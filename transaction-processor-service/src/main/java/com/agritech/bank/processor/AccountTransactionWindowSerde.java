package com.agritech.bank.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class AccountTransactionWindowSerde implements Serde<AccountTransactionWindow> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<AccountTransactionWindow> serializer() {
        return new Serializer<AccountTransactionWindow>() {
            @Override
            public byte[] serialize(String topic, AccountTransactionWindow data) {
                if (data == null) {
                    return null;
                }
                try {
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new RuntimeException("Error serializing AccountTransactionWindow", e);
                }
            }
        };
    }

    @Override
    public Deserializer<AccountTransactionWindow> deserializer() {
        return new Deserializer<AccountTransactionWindow>() {
            @Override
            public AccountTransactionWindow deserialize(String topic, byte[] data) {
                if (data == null) {
                    return null;
                }
                try {
                    return objectMapper.readValue(data, AccountTransactionWindow.class);
                } catch (Exception e) {
                    throw new RuntimeException("Error deserializing AccountTransactionWindow", e);
                }
            }
        };
    }
}