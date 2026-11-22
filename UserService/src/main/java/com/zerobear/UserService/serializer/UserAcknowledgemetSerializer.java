package com.zerobear.UserService.serializer;

import com.zerobear.UserService.dto.UserRegisterEvent;
import com.zerobear.UserService.dto.UserRegisterResponseEvent;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class UserAcknowledgemetSerializer implements Serializer<UserRegisterResponseEvent> {

    private final ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public byte[] serialize(String s, UserRegisterResponseEvent userRegisterResponseEvent) {

        String seralized= null;
        try {
            seralized = objectMapper.writeValueAsString(userRegisterResponseEvent);
        } catch (JacksonException e) {
            System.out.println("Unable to seralize the Object ");
        }
        return seralized.getBytes();
    }
}
