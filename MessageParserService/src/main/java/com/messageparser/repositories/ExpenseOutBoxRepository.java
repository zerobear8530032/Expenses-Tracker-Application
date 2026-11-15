package com.messageparser.repositories;

import com.messageparser.entities.ExpenseOutBox;
import com.messageparser.enums.ExpenseOutBoxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ExpenseOutBoxRepository extends JpaRepository<ExpenseOutBox,Long> {
    List<ExpenseOutBox> findAllByStatus(ExpenseOutBoxStatus status);
    @Modifying
    @Query("""
    DELETE FROM ExpenseOutBox e
    WHERE e.status = :status
    AND e.createdAt < :cutoff
""")
    int deleteOldPublishedEvents(
            @Param("status") ExpenseOutBoxStatus status,
            @Param("cutoff") Instant cutoff
    );
}
