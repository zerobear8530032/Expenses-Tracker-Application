package com.zerobear.deserializer;

import com.zerobear.dto.ExpenseEvent;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class ExpenseDeserializer implements Deserializer<ExpenseEvent> {
    private final ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public ExpenseEvent deserialize(String s, byte[] bytes) {
        ExpenseEvent expenseEvent=null;
        try {
            expenseEvent= objectMapper.readValue(bytes,ExpenseEvent.class);
        } catch (JacksonException e) {
            System.out.println("Unable to deserialize the object "+e);
        }

        return expenseEvent ;
    }
}
