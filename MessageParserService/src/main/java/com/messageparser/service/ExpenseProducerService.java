package com.messageparser.service;

import com.messageparser.dto.Expense;
import com.messageparser.event.ExpenseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ExpenseProducerService {

    @Value("${kafka.events.expense.topic}")
    String topic ;
    private final KafkaTemplate<String, ExpenseEvent> kafkaTemplate;

    @Autowired
    public ExpenseProducerService(KafkaTemplate<String, ExpenseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, ExpenseEvent>> produce(Expense expense){
        ExpenseEvent expenseEvent= new ExpenseEvent();
        expenseEvent.setAmount(expense.getAmount());
        expenseEvent.setMerchant(expense.getMerchant());
        expenseEvent.setCurrency(expense.getCurrency());
        expenseEvent.setCreatedBy(expense.getCreatedBy());
        expenseEvent.setEventId(expense.getEventId());
        expenseEvent.setUserId(expense.getUserId());
        Message<ExpenseEvent> message = MessageBuilder
                .withPayload(expenseEvent)
                .setHeader("event-type", "user-add-expense-event")
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, expense.getUserId())
                .build();
        return kafkaTemplate.send(message);
    }

}
