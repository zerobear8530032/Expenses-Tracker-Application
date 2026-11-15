package com.messageparser.service;

import com.messageparser.dto.Expense;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExpensesValidationService {
    public boolean isValidResponse(Expense expense) {

        if (expense == null) {
            return false;
        }
        if(expense.getUserId()==null){
            return false;
        }
        if (expense.getMerchant() == null ||
                expense.getMerchant().isBlank()) {
            return false;
        }

        if (expense.getCurrency() == null ||
                expense.getCurrency().isBlank()) {
            return false;
        }

        if (expense.getAmount() == null ||
                expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return true;
    }



}
