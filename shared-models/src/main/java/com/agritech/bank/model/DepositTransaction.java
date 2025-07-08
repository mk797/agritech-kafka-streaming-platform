package com.agritech.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class DepositTransaction extends Transaction {

    @JsonProperty("depositMode")
    @NotBlank(message = "Deposit mode is required")
    private String depositMode; // CASH, CHECK, ELECTRONIC

    @JsonProperty("checkNumber")
    private String checkNumber; // Only for CHECK deposits

    @JsonProperty("depositorAccountId")
    private String depositorAccountId; // Only for ELECTRONIC deposits

    public DepositTransaction(String transactionId, String accountId, BigDecimal amount,
                              String currency, String location, String channel,
                              String depositMode, String checkNumber, String depositorAccountId) {
        super(transactionId, accountId, amount, currency, location, channel);
        this.depositMode = depositMode;
        this.checkNumber = checkNumber;
        this.depositorAccountId = depositorAccountId;
    }

    @Override
    @JsonIgnore
    public TransactionType getTransactionType() {
        return TransactionType.DEPOSIT;
    }

    public DepositTransaction() {
        super();
    }
    // Getters
    public String getDepositMode() { return depositMode; }
    public String getCheckNumber() { return checkNumber; }
    public String getDepositorAccountId() { return depositorAccountId; }
}