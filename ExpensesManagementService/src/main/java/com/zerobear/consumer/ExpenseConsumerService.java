package com.zerobear.consumer;

import com.zerobear.dto.ExpenseEvent;
import com.zerobear.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseConsumerService {

    private final ExpenseService expenseService;

    public ExpenseConsumerService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @KafkaListener(topics = "user-expense-event", groupId = "expense-group")
    public void consumeExpense(ExpenseEvent  expenseEvent){
        log.info("Recieved Expense Event expense Successfully: {}",expenseEvent);
        try{
            log.info("Expense Event processing initiated: {}",expenseEvent);
            expenseService.validateExpense(expenseEvent);
            expenseService.addExpense(expenseEvent);
            log.info("Expense Event processed successfully: {}",expenseEvent);
        }catch (Exception e){
            log.error("Failed to process Expense register : {}",
                    expenseEvent, e);
            throw e;
        }
    }

}
