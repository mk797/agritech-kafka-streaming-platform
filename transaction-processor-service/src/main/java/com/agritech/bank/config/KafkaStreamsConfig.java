package com.agritech.bank.config;

import com.agritech.bank.model.Transaction; // Make sure this import is present
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean(name = "defaultKafkaStreamsConfig")
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "agritech-transaction-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.agritech.bank.model");

        // ✅ THIS IS THE FINAL FIX:
        // Provide a default type for the value deserializer. This tells Kafka Streams
        // to deserialize incoming messages as the base Transaction class, allowing
        // Jackson to use the subtype annotations to create the correct object.
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Transaction.class);

        return new KafkaStreamsConfiguration(props);
    }
}