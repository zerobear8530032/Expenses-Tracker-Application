package com.messageparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense{
    private String merchant;
    private String currency;
    private BigDecimal amount;
}
