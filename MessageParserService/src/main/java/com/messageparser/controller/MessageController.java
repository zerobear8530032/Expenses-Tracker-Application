package com.messageparser.controller;

import com.messageparser.dto.Expense;
import com.messageparser.dto.MessageRequest;
import com.messageparser.service.ExpenseService;
import com.messageparser.service.MessageParserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final ExpenseService expenseService;

    public MessageController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @GetMapping
    public  String getResponse(){
        return "heelo";
    }

    @PostMapping
    public Expense parseMessage(@RequestBody MessageRequest request){
        Expense expense=expenseService.parseAndPublishEvent(request);
        System.out.println(expense);
        return expense;
    }
}
