package com.agritech.bank.processor;

import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This component is responsible for building the Kafka Streams topology.
 * It is automatically called by Spring after the necessary beans are created,
 * which avoids the circular dependency issue.
 */
@Component
public class TopologyBuilder {

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder, TransactionProcessorService processorService) {
        System.out.println("🚀 Building pipeline with Spring's StreamsBuilder...");
        processorService.buildTopologyWith(streamsBuilder);
    }
}