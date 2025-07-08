package com.agritech.bank.api.service;

import com.agritech.bank.api.config.KafkaProducerConfig;
import com.agritech.bank.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProducerService {

    private final KafkaProducerConfig kafkaProducerConfig;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public String publishTransaction(Transaction transaction) {
        ProducerRecord<String, Transaction> record = new ProducerRecord<>(
                kafkaProducerConfig.getTopic(),
                transaction.getAccountId(),
                transaction
        );

        try {
            SendResult<String, Transaction> sendResult = kafkaTemplate.send(record).get(30, TimeUnit.SECONDS);

            log.info("Transaction {} sent successfully to partition {} at offset {}",
                    transaction.getTransactionId(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().offset());

            return "SUCCESS";

        } catch (ExecutionException e) {
            Throwable rootCause = e.getCause();

            if (rootCause instanceof TimeoutException) {
                log.warn("Transaction {} timed out", transaction.getTransactionId());
                return "Request timeout, please try again later";
            } else if (rootCause instanceof org.apache.kafka.common.errors.TopicAuthorizationException) {
                log.error("Topic authorization error for transaction: {}", transaction.getTransactionId());
                return "System configuration error. Contact support";
            } else {
                log.error("Failed to send transaction {}: {}", transaction.getTransactionId(), rootCause.getMessage());
                return "Transaction failed, please try again later";
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Transaction {} was interrupted", transaction.getTransactionId());
            return "Transaction is interrupted, please try again later";
        }
        catch (TimeoutException e) {
            // Handles the 30-second timeout from get() method
            log.warn("Transaction {} timed out at CompletableFuture level", transaction.getTransactionId());
            return "Request timeout, please try again later";
        }

    }
}
