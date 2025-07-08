package com.agritech.bank.api.controller;


import com.agritech.bank.api.dto.TransactionResponse;
import com.agritech.bank.api.service.TransactionProducerService;
import com.agritech.bank.model.DepositTransaction;
import com.agritech.bank.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionProducerService transactionProducerService;
    private final ObjectMapper objectMapper; // Inject the ObjectMapper

    // Fix: Add constructor injection for both dependencies
    @Autowired
    public TransactionController(TransactionProducerService transactionProducerService, ObjectMapper objectMapper) {
        this.transactionProducerService = transactionProducerService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> submitTransaction(
            @RequestBody @Valid Transaction transaction) {

        if (transaction.getTimestamp() == null) {
            transaction.setTimestamp(LocalDateTime.now());
        }

        logger.info("Received transaction submission: {}", transaction.getTransactionId());

        try {
            // Call your producer service (we'll build this next)
            String result = transactionProducerService.publishTransaction(transaction);

            TransactionResponse response = new TransactionResponse(
                    transaction.getTransactionId(),
                    "ACCEPTED",
                    "Transaction submitted successfully",
                    System.currentTimeMillis()
            );

            logger.info("Transaction {} submitted successfully", transaction.getTransactionId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to submit transaction {}: {}", transaction.getTransactionId(), e.getMessage());

            TransactionResponse errorResponse = new TransactionResponse(
                    transaction.getTransactionId(),
                    "FAILED",
                    "Internal server error: " + e.getMessage(),
                    System.currentTimeMillis()
            );

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("AgriTech Transaction API is operational");
    }

    @PostMapping("/test-json")
    public String testSerialization(@RequestBody DepositTransaction transaction) {
        try {
            // Use the injected objectMapper
            return objectMapper.writeValueAsString(transaction);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}