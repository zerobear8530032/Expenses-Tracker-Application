package com.messageparser.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class KafkaConfig {
        @Value("${kafka.events.expense.topic}")
        String topic;
        @Bean
        public NewTopic expenseCreatedTopic() {
            return TopicBuilder
                    .name(topic)
                    .partitions(1)
                    .replicas(1)
                    .build();
        }

        public ObjectMapper objectMapper(){
            return new ObjectMapper();
        }
}
