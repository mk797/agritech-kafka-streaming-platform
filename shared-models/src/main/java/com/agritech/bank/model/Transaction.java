package com.agritech.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "transactionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DepositTransaction.class, name = "DEPOSIT"),
        @JsonSubTypes.Type(value = WithdrawalTransaction.class, name = "WITHDRAWAL"),
        @JsonSubTypes.Type(value = BillPaymentTransaction.class, name = "BILL_PAYMENT"),
        @JsonSubTypes.Type(value = SalaryTransaction.class, name = "SALARY")
})
public abstract class Transaction {

    @JsonProperty("transactionId")
    @NotBlank(message = "Transaction ID is required")
    String transactionId;

    @JsonProperty("accountId")
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^AGRI-[A-Z0-9]{8}$", message = "Invalid AgriTech account format")
    private String accountId;

    @JsonProperty("amount")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @JsonProperty("currency")
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^(USD|EUR|GBP|INR)$", message = "Unsupported currency")
    private String currency;

    @Setter
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("location")
    @Size(max = 100, message = "Location too long")
    private String location;

    @JsonProperty("channel")
    @NotBlank(message = "Transaction channel is required")
    private String channel;

    @JsonProperty("status")
    private TransactionStatus status;


    public abstract TransactionType getTransactionType();

    protected Transaction() {
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

    protected Transaction(String transactionId, String accountId, BigDecimal amount,
                          String currency, String location, String channel) {
        this(); // Chain to the no-arg constructor to set timestamp and status
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.location = location;
        this.channel = channel;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }


    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLocation() { return location; }
    public String getChannel() { return channel; }
    public TransactionStatus getStatus() { return status; }

    public void setStatus(TransactionStatus status) { this.status = status; }

}