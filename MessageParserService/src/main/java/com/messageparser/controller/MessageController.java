package com.messageparser.controller;

import com.messageparser.dto.Expense;
import com.messageparser.dto.MessageRequest;
import com.messageparser.service.MessageParserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessageParserService messageParserService;

    public MessageController(MessageParserService messageParserService) {
        this.messageParserService = messageParserService;
    }

    @GetMapping
    public  String getResponse(){
        return "heelo";
    }

    @PostMapping
    public Expense parseMessage(@RequestBody MessageRequest request){
        Expense expense=messageParserService.parseExpense(request);
        System.out.println(expense);
        return expense;
    }
}
