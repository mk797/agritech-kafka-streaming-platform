package com.agritech.bank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SalaryTransaction  extends Transaction{

    @JsonProperty("payPeriod")
    @NotBlank(message = "Pay period is required")
    private String payPeriod;

    @JsonProperty("employerId") // Fixed naming convention
    @NotBlank(message = "Employer ID is required")
    private String employerId; // Fixed variable name

    @JsonProperty("payrollReference")
    private String payrollReference; // Additional field

    public SalaryTransaction(String transactionId, String accountId, BigDecimal amount,
                             String currency, String location, String channel,
                             String payPeriod, String employerId, String payrollReference) {
        super(transactionId, accountId, amount, currency, location, channel);
        this.payPeriod = payPeriod;
        this.employerId = employerId;
        this.payrollReference = payrollReference;
    }

    public SalaryTransaction() {
        super();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SALARY;
    }

    // Add getters
    public String getPayPeriod() { return payPeriod; }
    public String getEmployerId() { return employerId; }
    public String getPayrollReference() { return payrollReference; }

}
