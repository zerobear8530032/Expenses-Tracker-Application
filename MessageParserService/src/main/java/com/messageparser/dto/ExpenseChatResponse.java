package com.messageparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseChatResponse {
    private String merchant;
    private String currency;
    private BigDecimal amount;
}
