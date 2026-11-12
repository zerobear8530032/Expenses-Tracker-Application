package com.zerobear.serializer;

import com.zerobear.dto.ExpenseEvent;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

public class ExpenseEventSerializer implements Serializer<ExpenseEvent> {

    private final ObjectMapper objectMapper= new ObjectMapper();

    @Override
    public byte[] serialize(String s, ExpenseEvent expenseEvent) {
        String serialize=null;
        try{
            serialize=objectMapper.writeValueAsString(expenseEvent);
        }catch (Exception e){
            e.printStackTrace();
        }
        return serialize.getBytes();
    }
}