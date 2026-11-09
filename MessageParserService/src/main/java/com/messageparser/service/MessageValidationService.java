package com.messageparser.service;

import com.messageparser.exception.InvalidMessageException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MessageValidationService {
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
}
