package com.agritech.bank.model;

public enum TransactionType {

    DEPOSIT("Account Deposit"),
    WITHDRAWAL("Account Withdrawal"),
    BILL_PAYMENT("Bill Payment"),
    SALARY("Salary Credit");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}
