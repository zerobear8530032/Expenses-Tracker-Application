package com.messageparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense{
    private String eventId;
    private String merchant;
    private String userId;
    private String currency;
    private BigDecimal amount;
    private String createdBy;
    private Instant createdAt;
}
