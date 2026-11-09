package com.messageparser.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseEvent {
    private String merchant;
    private String currency;
    private BigDecimal amount;
    private String createdBy ;
}
