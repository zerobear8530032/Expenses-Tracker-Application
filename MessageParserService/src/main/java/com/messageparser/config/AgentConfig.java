package com.messageparser.config;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class AgentConfig {


    @Bean
    public ChatClient ollamaClient(OllamaChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel).build();
    }
}
