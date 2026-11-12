package com.zerobear.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEvent {
    private BigDecimal amount;
    private String merchant;
    private String currency;
    private String createdBy;
}
