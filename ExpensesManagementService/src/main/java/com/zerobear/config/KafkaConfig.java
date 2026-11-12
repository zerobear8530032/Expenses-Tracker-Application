package com.zerobear.config;

import com.zerobear.dto.ExpenseEvent;
import com.zerobear.exception.InvalidExpenseEventException;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
@Configuration
public class KafkaConfig {
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, ExpenseEvent> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer= new DeadLetterPublishingRecoverer(kafkaTemplate);
        FixedBackOff backOff = new FixedBackOff(5000L, 2L);
        DefaultErrorHandler errorHandler= new DefaultErrorHandler(recoverer,backOff);
        errorHandler.addNotRetryableExceptions(InvalidExpenseEventException.class);
        return errorHandler;
    }

}
