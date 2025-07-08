package com.agritech.bank.processor;

import com.agritech.bank.model.Transaction;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionProcessorService {


    public void buildTopologyWith(StreamsBuilder builder) {

        System.out.println("🔧 Building topology with Spring-managed StreamsBuilder...");

        KStream<String, Transaction> transactionKStream = builder.stream("agritech-transactions");
        System.out.println("✅ Transaction stream created from topic: agritech-transactions");

        // State store configuration
        StoreBuilder<KeyValueStore<String, AccountTransactionWindow>> storeBuilder =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("account-transaction-store"),
                        Serdes.String(),
                        new AccountTransactionWindowSerde()
                );
        builder.addStateStore(storeBuilder);
        System.out.println("✅ State store added");

        // Individual fraud detection
        KStream<String, Transaction> individualFraud = transactionKStream
                .filter((key, transaction) -> {
                    // Amount check
                    boolean largeAmount = transaction.getAmount().compareTo(BigDecimal.valueOf(10000)) >= 0;

                    // Time check - handle null timestamp
                    boolean suspiciousTime = false;
                    if (transaction.getTimestamp() != null) {
                        int hourOfDay = transaction.getTimestamp().getHour();
                        suspiciousTime = hourOfDay >= 2 && hourOfDay <= 5;
                    }

                    return largeAmount || suspiciousTime;
                });
        System.out.println("✅ Individual fraud stream created");

        // Velocity fraud detection
        KStream<String, Transaction> velocityFraud = transactionKStream
                .transformValues(() -> new VelocityFraudTransformer(), "account-transaction-store")
                .filter((key, value) -> value != null);
        System.out.println("✅ Velocity fraud stream created");

        // Output streams
        individualFraud.to("agritech-individual-fraud-alerts");
        velocityFraud.to("agritech-velocity-fraud-alerts");
        transactionKStream.to("agritech-audit-trail");
        System.out.println("✅ Output streams configured");

        System.out.println("🎯 Topology configuration complete!");
    }
}

