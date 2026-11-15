package com.messageparser.schedulers;

import com.messageparser.dto.Expense;
import com.messageparser.entities.ExpenseOutBox;
import com.messageparser.enums.ExpenseOutBoxStatus;
import com.messageparser.event.ExpenseEvent;
import com.messageparser.repositories.ExpenseOutBoxRepository;
import com.messageparser.service.ExpenseProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ExpenseEventScheduler {

    private final ExpenseOutBoxRepository expenseOutBoxRepository;
    private final ExpenseProducerService expenseProducerService;
    private final ObjectMapper objectMapper;
    public ExpenseEventScheduler(ExpenseOutBoxRepository expenseOutBoxRepository, ExpenseProducerService expenseProducerService, ObjectMapper objectMapper) {
        this.expenseOutBoxRepository = expenseOutBoxRepository;
        this.expenseProducerService = expenseProducerService;
        this.objectMapper = objectMapper;
    }

    public Expense parseJson(String json)  throws JacksonException{
        return objectMapper.readValue(json, Expense.class);
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void executeTask() {
        System.out.println("Scheduler executed: " + LocalDateTime.now());
       List<ExpenseOutBox> expenseOutBoxes=expenseOutBoxRepository.findAllByStatus(ExpenseOutBoxStatus.PENDING);
       for(ExpenseOutBox expenseOutBox: expenseOutBoxes){
           try{
               String json  = expenseOutBox.getEventJson();
               Expense event= parseJson(json);
               try {
                   expenseProducerService
                           .produce(event)
                           .get();

                   expenseOutBox.setStatus(ExpenseOutBoxStatus.PUBLISHED);
                   expenseOutBoxRepository.save(expenseOutBox);

                   log.info("Published event: {}", event.getEventId());

               } catch (Exception ex) {
                   log.error("Failed to publish event: {}", event.getEventId(), ex);
               }
               log.info("Produce event {}",expenseOutBox);
           }catch (JacksonException ex){
               log.error("cannot parse given json",ex);
               expenseOutBox.setStatus(ExpenseOutBoxStatus.FAILED);
               expenseOutBoxRepository.saveAndFlush(expenseOutBox);
           }
       }
    }
}
