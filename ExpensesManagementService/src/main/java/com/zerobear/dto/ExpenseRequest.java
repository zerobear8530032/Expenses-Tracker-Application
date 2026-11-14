package com.zerobear.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpenseRequest {
    private String eventId;
    private String userId;
    private BigDecimal amount;
    private String merchant;
    private String currency;
    private String createdBy;
}
