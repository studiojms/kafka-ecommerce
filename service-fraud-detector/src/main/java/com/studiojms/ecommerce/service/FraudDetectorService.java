package com.studiojms.ecommerce.service;

import com.studiojms.ecommerce.core.KafkaService;
import com.studiojms.ecommerce.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author jefferson.souza
 */
public class FraudDetectorService {

    private final static String TOPIC_NAME = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) throws InterruptedException {
        final var fraudDetectorService = new FraudDetectorService();
        try (var kafkaService = new KafkaService<>(FraudDetectorService.class.getSimpleName(), TOPIC_NAME,
                fraudDetectorService::parse, Order.class)) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Order was processed.");
    }

}
