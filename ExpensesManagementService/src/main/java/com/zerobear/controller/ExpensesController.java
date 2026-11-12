package com.zerobear.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpensesController {


    @GetMapping
    public String check(){
        return  "Welcome to Expense Management Appliction";
    }
}
