package com.zerobear.ExpensesManagementApplication.deserializer;

import com.zerobear.ExpensesManagementApplication.dto.UserRegisterEvent;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class UserEventDeserializer implements Deserializer<UserRegisterEvent> {

    private final ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public UserRegisterEvent deserialize(String s, byte[] bytes) {
            UserRegisterEvent userRegisterEvent=null;
        try {
                    userRegisterEvent= objectMapper.readValue(s,UserRegisterEvent.class);
        } catch (JacksonException e) {
            System.out.println("Unable to deserialize the object "+e);
        }

        return userRegisterEvent ;
    }
}
