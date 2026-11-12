package com.zerobear.service;

import com.zerobear.dto.ExpenseEvent;
import com.zerobear.dto.ExpenseResponse;
import com.zerobear.entities.Expense;
import com.zerobear.exception.InvalidExpenseEventException;
import com.zerobear.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
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
    public ExpenseResponse addExpense(ExpenseEvent expenseEvent){
        Expense expense= new Expense();
        expense.setAmount(expenseEvent.getAmount());
        expense.setCurrency(expenseEvent.getCurrency());
        expense.setMerchant(expenseEvent.getMerchant());
        expense.setUserId("abc");
        expense.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Expense savedExpense=expenseRepository.save(expense);
        ExpenseResponse expenseResponse= new ExpenseResponse();
        expenseResponse.setId(savedExpense.getId());
        expenseResponse.setAmount(savedExpense.getAmount());
        expenseResponse.setCreatedAt(savedExpense.getCreatedAt());
        expenseResponse.setCurrency(savedExpense.getCurrency());
        expenseResponse.setUserId(savedExpense.getUserId());
        return expenseResponse;
    }
}
