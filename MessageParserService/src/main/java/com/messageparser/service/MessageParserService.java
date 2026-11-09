package com.messageparser.service;

import com.messageparser.dto.Expense;
import com.messageparser.dto.MessageRequest;
import com.messageparser.exception.InvalidExpenseException;
import com.messageparser.exception.InvalidMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Arrays;
import java.util.List;

@Service
public class MessageParserService {

    @Value("classpath:static/prompt.txt")
    private Resource prompt;
    private final ExpensesValidationService expensesService;
    private  final ChatClient chatClient;

    @Autowired
    public MessageParserService(ExpensesValidationService expensesService, @Qualifier("ollamaClient") ChatClient chatClient) {
        this.expensesService = expensesService;
        this.chatClient = chatClient;
    }



    public Expense extractExpense(String message) {
        Expense expense = chatClient.prompt()
                .system(prompt)
                .user(message)
                .call()
                .entity(Expense.class);
        if (expensesService.isValid(expense)) {
            return expense;
        }
        throw new InvalidExpenseException("Invalid Expenses generated");
    }

}
