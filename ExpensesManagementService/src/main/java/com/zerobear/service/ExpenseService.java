package com.zerobear.service;

import com.zerobear.dto.ExpenseEvent;
import com.zerobear.dto.ExpenseRequest;
import com.zerobear.dto.ExpenseResponse;
import com.zerobear.entities.Expense;
import com.zerobear.entities.Idempotency;
import com.zerobear.enums.IdempotencyStatus;
import com.zerobear.exception.InvalidExpenseEventException;
import com.zerobear.repository.ExpenseRepository;
import com.zerobear.repository.IdempotencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, IdempotencyRepository idempotencyRepository, ObjectMapper objectMapper) {
        this.expenseRepository = expenseRepository;
        this.idempotencyRepository = idempotencyRepository;

        this.objectMapper = objectMapper;
    }

    public boolean validateExpense (ExpenseEvent expenseEvent) throws InvalidExpenseEventException {
        if(expenseEvent.getAmount()==null || expenseEvent.getAmount().compareTo(new BigDecimal(0))<=0){
            throw new InvalidExpenseEventException("Not Valid Amount");
        }
        if(expenseEvent.getCurrency()==null){
            throw new InvalidExpenseEventException("Not Valid Currency");
        }
        if(expenseEvent.getMerchant()==null){
            throw new InvalidExpenseEventException("Not Valid Merchant");
        }
        if (expenseEvent.getCreatedBy()==null){
            throw new InvalidExpenseEventException("Not Valid Author");
        }
        return true;
    }
    @Transactional
    public void addExpenseEvent(ExpenseEvent expenseEvent){
        if (expenseRepository.existsByEventId(expenseEvent.getEventId())) {
            log.info("Duplicate event received: {}", expenseEvent.getEventId());
            return;
        }
        Expense expense= new Expense();
        expense.setAmount(expenseEvent.getAmount());
        expense.setCurrency(expenseEvent.getCurrency());
        expense.setMerchant(expenseEvent.getMerchant());
        expense.setEventId(expenseEvent.getEventId());
        expense.setUserId("abc");
        expense.setCreatedAt(Instant.now());
        Expense savedExpense=expenseRepository.saveAndFlush(expense);
        ExpenseResponse expenseResponse= new ExpenseResponse();
        expenseResponse.setId(savedExpense.getId());
        expenseResponse.setAmount(savedExpense.getAmount());
        expenseResponse.setCreatedAt(savedExpense.getCreatedAt());
        expenseResponse.setCurrency(savedExpense.getCurrency());
        expenseResponse.setUserId(savedExpense.getUserId());

    }

    @Transactional
    public ExpenseResponse addExpense(ExpenseRequest expenseRequest,String key) throws NoSuchAlgorithmException{
        Optional<Idempotency> optionalIdempotency=idempotencyRepository.findByUserIdAndIdempotencyKey(expenseRequest.getUserId(),key);
        String hashRequest= hashRequest(expenseRequest);
        if(optionalIdempotency.isPresent()){
            Idempotency idempotency= optionalIdempotency.get();
            if(!hashRequest.equals(idempotency.getRequestHash())){
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
            // Then validate status
            if (idempotency.getStatus() != IdempotencyStatus.COMPLETED) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }

            return objectMapper.readValue(
                    idempotency.getResponseBody(),
                    ExpenseResponse.class
            );
        }
        String jsonRequest = toJson(expenseRequest);
        Idempotency idempotency = new Idempotency();
        idempotency.setRequestBody(jsonRequest);
        idempotency.setStatus(IdempotencyStatus.IN_PROGRESS);
        idempotency.setUserId(expenseRequest.getUserId());
        idempotency.setRequestHash(hashRequest);
        idempotency.setIdempotencyKey(key);
        Idempotency savedIdempotency=idempotencyRepository.saveAndFlush(idempotency);

        Expense expense= new Expense();
        expense.setUserId(expenseRequest.getUserId());
        expense.setCurrency(expenseRequest.getCurrency());
        expense.setAmount(expenseRequest.getAmount());
        expense.setMerchant(expenseRequest.getMerchant());
        expense.setCreatedAt(Instant.now());
        expense.setEventId(generateId());
        Expense savedExpense=creatExpense(expense);

        ExpenseResponse expenseResponse= new ExpenseResponse();
        expenseResponse.setId(savedExpense.getId());
        expenseResponse.setMerchant(savedExpense.getMerchant());
        expenseResponse.setUserId(savedExpense.getUserId());
        expenseResponse.setCurrency(savedExpense.getCurrency());
        expenseResponse.setAmount(savedExpense.getAmount());
        expenseResponse.setCreatedBy("USER");
        expenseResponse.setCreatedAt(savedExpense.getCreatedAt());
        savedIdempotency.setHttpStatus(HttpStatus.CREATED);
        savedIdempotency.setResponseBody(toJson(expenseResponse));
        savedIdempotency.setStatus(IdempotencyStatus.COMPLETED);
        idempotencyRepository.save(savedIdempotency);
        return expenseResponse;
    }



    public Expense creatExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    private String toJson(Object o){
        return  objectMapper.writeValueAsString(o);
    }
    private String generateId(){
        return UUID.randomUUID().toString();
    }

    private String hashRequest(ExpenseRequest expenseRequest) throws NoSuchAlgorithmException {
        byte[] requestBytes=objectMapper.writeValueAsBytes(expenseRequest);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestBytes);

        return HexFormat.of().formatHex(hash);
    }
}
