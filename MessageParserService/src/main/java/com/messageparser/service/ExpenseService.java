package com.messageparser.service;

import com.messageparser.entities.ExpenseOutBox;
import com.messageparser.dto.Expense;
import com.messageparser.dto.MessageRequest;
import com.messageparser.enums.ExpenseOutBoxStatus;
import com.messageparser.exception.InvalidExpenseException;
import com.messageparser.exception.InvalidMessageException;
import com.messageparser.repositories.ExpenseOutBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseProducerService expenseProducerService;
    private final ExpensesValidationService expensesValidationService;
    private final MessageValidationService messageValidationService;
    private final MessageParserService messageParserService;
    private final ExpenseOutBoxRepository expenseOutBoxRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExpenseService(ExpenseProducerService expenseProducerService, ExpensesValidationService expensesValidationService, MessageValidationService messageValidationService, MessageParserService messageParserService, ExpenseOutBoxRepository expenseOutBoxRepository, ObjectMapper objectMapper) {
        this.expenseProducerService = expenseProducerService;
        this.expensesValidationService = expensesValidationService;
        this.messageValidationService = messageValidationService;
        this.messageParserService = messageParserService;
        this.expenseOutBoxRepository = expenseOutBoxRepository;
        this.objectMapper = objectMapper;
    }

    private Expense dummyExpense(){
        Expense expense= new Expense();
        expense.setAmount(new BigDecimal(71));
        expense.setCurrency("INR");
        expense.setMerchant("HDFC");
        expense.setCreatedBy("AI");
        expense.setUserId("User-123");
        expense.setEventId(UUID.randomUUID().toString());
        expense.setCreatedAt(Instant.now());
        return expense;
    }

    @Transactional
    public Expense parseAndPublishEvent(String userId,MessageRequest messageRequest){
        String message= messageRequest.getMessage();
        if(messageValidationService.isValidBankMessage(message)){
//            Expense expense= messageParserService.extractExpense(,message);
            Expense expense= dummyExpense();
            if(expensesValidationService.isValidResponse(expense)){
                createExpenseOutBoxRecord(expense);
                return expense;
            }else{
                throw  new InvalidExpenseException("Expenses generated is invalid");
            }
        }else{
            throw new InvalidMessageException("Message is valid ");
        }
    }

    public void publishEvent(Expense expense) {
       expenseProducerService.produce(expense);
    }

    private String stringify(Expense expense)  {
        String json = objectMapper.writeValueAsString(expense);
        return json;
    }

    public void createExpenseOutBoxRecord(Expense expense){
        String json= stringify(expense);
        ExpenseOutBox expenseOutBox = new ExpenseOutBox();
        expenseOutBox.setEventJson(json);
        expenseOutBox.setStatus(ExpenseOutBoxStatus.PENDING);
        expenseOutBox.setCreatedAt(Instant.now());
        expenseOutBoxRepository.saveAndFlush(expenseOutBox);
    }
}
