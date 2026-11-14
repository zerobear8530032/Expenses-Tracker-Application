package com.messageparser.service;

import com.messageparser.dto.Expense;
import com.messageparser.dto.ExpenseChatResponse;
import com.messageparser.exception.InvalidExpenseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

import java.time.Instant;
import java.util.UUID;

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
        ExpenseChatResponse expenseChatResponse = chatClient.prompt()
                .system(prompt)
                .user(message)
                .call()
                .entity(ExpenseChatResponse.class);
        Expense expense= new Expense();
        expense.setAmount(expenseChatResponse.getAmount());
        expense.setMerchant(expense.getMerchant());
        expense.setCurrency(expense.getCurrency());
        expense.setEventId(UUID.randomUUID().toString());
        expense.setCreatedBy("AI");
        expense.setCreatedAt(Instant.now());
        if (expensesService.isValidResponse(expense)) {
            return expense;
        }
        throw new InvalidExpenseException("Invalid Expenses generated");
    }

}
