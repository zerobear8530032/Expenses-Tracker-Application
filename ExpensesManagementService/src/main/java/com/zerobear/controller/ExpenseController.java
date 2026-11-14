package com.zerobear.controller;

import com.zerobear.dto.ExpenseEvent;
import com.zerobear.dto.ExpenseRequest;
import com.zerobear.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;


    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<?> createResponse(@RequestBody ExpenseRequest expenseRequest, @RequestHeader("Idempotency-Key") String key) throws NoSuchAlgorithmException {
        expenseService.addExpense(expenseRequest,key);
        return ResponseEntity.ok("");
    }

    @GetMapping("/check")
    public String healthCheck(){
        return  "Still alive";
    }

}
