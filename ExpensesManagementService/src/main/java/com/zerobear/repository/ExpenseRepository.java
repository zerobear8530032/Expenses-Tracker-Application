package com.zerobear.repository;

import com.zerobear.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    public boolean existsByEventId(String eventId);
}
