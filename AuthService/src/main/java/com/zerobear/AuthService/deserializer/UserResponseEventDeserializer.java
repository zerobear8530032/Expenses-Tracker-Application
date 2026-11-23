package com.zerobear.AuthService.deserializer;

import com.zerobear.AuthService.dto.UserRegisterEvent;
import com.zerobear.AuthService.dto.UserRegisterResponseEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class UserResponseEventDeserializer implements Deserializer<UserRegisterResponseEvent> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public UserRegisterResponseEvent deserialize(String topic, byte[] bytes) {

            if (bytes == null) {
                return null;
            }

            try {
                return objectMapper.readValue(
                        bytes,
                        UserRegisterResponseEvent.class
                );

            } catch (JacksonException e) {
                throw new SerializationException(
                        "Unable to deserialize user acknowledgement",
                        e
                );
            }
        }
    }

