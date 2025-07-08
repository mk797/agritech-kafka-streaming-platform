package com.agritech.bank.api;


import com.agritech.bank.api.config.KafkaProducerConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableKafka
public class TransactionApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(TransactionApiApplication.class);

    public static void main(String[] args) {
        logger.info("🏦 Starting AgriTech Bank Transaction API Service...");
        SpringApplication.run(TransactionApiApplication.class, args);
        logger.info("🚀 AgriTech Transaction API Service is now operational on port 8080");
    }


    @Bean
    NewTopic transactionsTopic(@Value("${agritech.kafka.topics.transactions}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(4)
                .replicas(1)
                .build();
    }
}