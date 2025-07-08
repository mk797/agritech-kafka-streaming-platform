package com.agritech.bank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class WithdrawalTransaction extends Transaction{

    @JsonProperty("cardNumber")
    @Pattern(regexp = "^[0-9]{16}$", message = "Invalid card number format")
    private String cardNumber; // Changed to String

    @JsonProperty("withdrawalMode")
    @NotBlank(message = "Withdrawal Mode is required")
    private String withdrawalMode; // ATM, BRANCH, ONLINE

    @JsonProperty("atmLocation")
    private String atmLocation; // Optional - only for ATM withdrawals

    public WithdrawalTransaction(String transactionId, String accountId, BigDecimal amount,
                                 String currency, String location, String channel,
                                 String cardNumber, String withdrawalMode, String atmLocation) {
        super(transactionId, accountId, amount, currency, location, channel);
        this.cardNumber = cardNumber;
        this.withdrawalMode = withdrawalMode;
        this.atmLocation = atmLocation;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.WITHDRAWAL; // Fixed!
    }

    public WithdrawalTransaction() {
        super();
    }

    // Add getters
    public String getCardNumber() { return cardNumber; }
    public String getWithdrawalMode() { return withdrawalMode; }
    public String getAtmLocation() { return atmLocation; }
}