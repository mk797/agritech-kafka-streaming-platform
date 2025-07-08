package com.agritech.bank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class BillPaymentTransaction extends Transaction{

    @JsonProperty("billId")
    @NotBlank(message = "Bill ID is required")
    private String billId;

    @JsonProperty("payeeId")
    @NotBlank(message = "Payee ID is required")
    private String payeeId;

    // Fixed constructor to include new fields
    public BillPaymentTransaction(String transactionId, String accountId, BigDecimal amount,
                                  String currency, String location, String channel,
                                  String billId, String payeeId) {
        super(transactionId, accountId, amount, currency, location, channel);
        this.billId = billId;
        this.payeeId = payeeId;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.BILL_PAYMENT;
    }

    public BillPaymentTransaction() {
        super();
    }
    // Add getters
    public String getBillId() { return billId; }
    public String getPayeeId() { return payeeId; }
}
