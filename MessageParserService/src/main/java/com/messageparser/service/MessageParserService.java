package com.messageparser.service;

import com.messageparser.dto.Expense;
import com.messageparser.dto.MessageRequest;
import com.messageparser.exception.InvalidExpenseException;
import com.messageparser.exception.InvalidMessageException;
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
    private final ExpensesService expensesService;

    private  final ChatClient chatClient;
    private static final List<String> FINANCIAL_KEYWORDS = List.of(
            "account", "bank", "upi", "card", "payment"
    );

    private static final List<String> TRANSACTION_KEYWORDS = List.of(
            "debited", "credited", "transferred","transaction",
            "withdrawn", "deposited", "paid",
            "received", "purchase", "refund"
    );

    private static final List<String> PROMOTIONAL_KEYWORDS = List.of(
            "cashback offer", "limited offer",
            "win exciting prizes", "shop now"
    );

    public MessageParserService(ExpensesService expensesService,@Qualifier("ollamaClient") ChatClient chatClient) {
        this.expensesService = expensesService;
        this.chatClient = chatClient;
    }

    public boolean isValidBankMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new InvalidMessageException("Message cannot be empty");
        }
        if(message.length()>500){
            throw new InvalidMessageException("Message is too long");
        }

        String normalized = message.toLowerCase();

        boolean hasFinancialContext = FINANCIAL_KEYWORDS.stream()
                .anyMatch(normalized::contains);

        boolean hasTransactionActivity = TRANSACTION_KEYWORDS.stream()
                .anyMatch(normalized::contains);

        boolean isPromotional = PROMOTIONAL_KEYWORDS.stream()
                .anyMatch(normalized::contains);

        long wordCount = Arrays.stream(message.trim().split("\\s+"))
                .count();

        if (wordCount > 125) {
            throw new InvalidMessageException("Message exceeds maximum word limit");
        }

        if(!(hasFinancialContext && hasTransactionActivity && !isPromotional)){
            throw new InvalidMessageException("Your message is not a valid transaction");
        }

        return true;
    }

    public Expense parseExpense(MessageRequest message){
        isValidBankMessage(message.getMessage());
        return extractExpense(message.getMessage());
    }

    public Expense extractExpense(String message){
        Expense expense=chatClient.prompt()
                .system(prompt)
                .user(message)
                .call()
                .entity(Expense.class);
        if (expensesService.isValid(expense)){
            return expense;
        }
        throw new InvalidExpenseException("Invalid Expenses generated");
    }
}
