package com.zerobear.AuthService.deserializer;

import com.zerobear.AuthService.dto.UserRegisterEvent;
import com.zerobear.AuthService.dto.UserRegisterResponseEvent;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class UserResponseEventDeserializer implements Deserializer<UserRegisterResponseEvent> {

    private final ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public UserRegisterResponseEvent deserialize(String s, byte[] bytes) {
        UserRegisterResponseEvent userRegisterEvent=null;
        try {
                    userRegisterEvent= objectMapper.readValue(bytes,UserRegisterResponseEvent.class);
        } catch (JacksonException e) {
            System.out.println("Unable to deserialize the object "+e);
        }

        return userRegisterEvent ;
    }
}
