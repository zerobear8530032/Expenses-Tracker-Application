package com.zerobear.consumer;

import com.zerobear.dto.ExpenseEvent;
import com.zerobear.repository.ExpenseRepository;
import com.zerobear.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseConsumerService {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;
    public ExpenseConsumerService(ExpenseService expenseService, ExpenseRepository expenseRepository) {
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
    }

    @KafkaListener(topics = "user-expense-event", groupId = "expense-group")
    public void consumeExpense(ExpenseEvent  expenseEvent){
        try {
            expenseService.addExpenseEvent(expenseEvent);

            log.info("Expense processed: {}", expenseEvent.getEventId());

        } catch (DataIntegrityViolationException e) {

            if (expenseRepository.existsByEventId(expenseEvent.getEventId())) {
                log.info("Duplicate event ignored: {}", expenseEvent.getEventId());
                return;
            }

            throw e;
        }

    }

}
