package com.messageparser.entities;



import com.messageparser.enums.ExpenseOutBoxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseOutBox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(columnDefinition = "JSON")
    String eventJson;
    @Enumerated(EnumType.STRING)
    ExpenseOutBoxStatus status;
    Instant createdAt;
}
