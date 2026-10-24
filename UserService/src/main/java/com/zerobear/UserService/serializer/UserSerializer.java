package com.zerobear.UserService.serializer;

import com.zerobear.UserService.dto.UserRegisterEvent;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class UserSerializer implements Serializer<UserRegisterEvent> {

    private final ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public byte[] serialize(String s, UserRegisterEvent userRegisterEvent) {

        String seralized= null;
        try {
            seralized = objectMapper.writeValueAsString(userRegisterEvent);
        } catch (JacksonException e) {
            System.out.println("Unable to seralize the Object ");
        }
        return seralized.getBytes();
    }
}
