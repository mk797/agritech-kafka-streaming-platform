package com.agritech.bank.processor;

import com.agritech.bank.model.Transaction;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.ZoneId;

public class VelocityFraudTransformer implements ValueTransformer<Transaction, Transaction> {

    private ProcessorContext context;
    private KeyValueStore<String, AccountTransactionWindow> stateStore;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.stateStore = (KeyValueStore<String, AccountTransactionWindow>)
                context.getStateStore("account-transaction-store");
    }

    @Override
    public Transaction transform(Transaction transaction) {
        String accountId = transaction.getAccountId();

        AccountTransactionWindow window = stateStore.get(accountId);
        if (window == null) {
            window = new AccountTransactionWindow();
        }

        long transactionTime;
        if (transaction.getTimestamp() != null) {
            transactionTime = transaction.getTimestamp()
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else {
            // Use current time if timestamp is null
            transactionTime = System.currentTimeMillis();
            System.out.println("⚠️ Transaction has null timestamp, using current time: " + transaction.getTransactionId());
        }

        boolean isFraud = window.checkFraud(transactionTime);
        window.addTransaction(transactionTime);
        stateStore.put(accountId, window);

        // Return transaction if fraud, null otherwise
        return isFraud ? transaction : null;
    }

    @Override
    public void close() {
        // Nothing to clean up
    }
}