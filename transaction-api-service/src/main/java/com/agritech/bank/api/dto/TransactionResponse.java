package com.agritech.bank.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionResponse {

    @JsonProperty("transactionId")
    private final String transactionId;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("timestamp")
    private final long timestamp;

    public TransactionResponse(String transactionId, String status, String message, long timestamp) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}