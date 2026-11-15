package com.messageparser.schedulers;

import com.messageparser.enums.ExpenseOutBoxStatus;
import com.messageparser.repositories.ExpenseOutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpenseOutBoxCleanupScheduler {

    private final ExpenseOutBoxRepository expenseOutBoxRepository;

    @Scheduled(cron = "0 0 2 */7 * *")
//    @Scheduled(fixedRate = 7000) -- testing
    @Transactional
    public void cleanup() {

        Instant cutoff = Instant.now().minus(7, ChronoUnit.DAYS);

        int deleted = expenseOutBoxRepository.deleteOldPublishedEvents(
            ExpenseOutBoxStatus.PUBLISHED,
            cutoff
        );

        log.info("Deleted {} old published outbox records", deleted);
    }
}