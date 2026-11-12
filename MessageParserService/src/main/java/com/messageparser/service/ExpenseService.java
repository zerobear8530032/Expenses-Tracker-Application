package com.messageparser.service;

import com.messageparser.dto.Expense;
import com.messageparser.dto.MessageRequest;
import com.messageparser.exception.InvalidExpenseException;
import com.messageparser.exception.InvalidMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseProducerService expenseProducerService;
    private final ExpensesValidationService expensesValidationService;
    private final MessageValidationService messageValidationService;
    private final MessageParserService messageParserService;

    @Autowired
    public ExpenseService(ExpenseProducerService expenseProducerService, ExpensesValidationService expensesValidationService, MessageValidationService messageValidationService, MessageParserService messageParserService) {
        this.expenseProducerService = expenseProducerService;
        this.expensesValidationService = expensesValidationService;
        this.messageValidationService = messageValidationService;
        this.messageParserService = messageParserService;
    }

    public Expense parseAndPublishEvent(MessageRequest messageRequest){
        String message= messageRequest.getMessage();
        if(messageValidationService.isValidBankMessage(message)){
            Expense expense= messageParserService.extractExpense(message);
            if(expensesValidationService.isValid(expense)){
                publishEvent("user-expense-event",expense);
                return expense;
            }else{
                throw  new InvalidExpenseException("Expenses generated is invalid");
            }
        }else{
            throw new InvalidMessageException("Message is valid ");
        }
    }

    public void publishEvent(String key,Expense expense) {
       expenseProducerService.produce(key,expense);
    }
}
