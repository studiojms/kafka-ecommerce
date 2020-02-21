package com.studiojms.ecommerce.service;

import com.studiojms.ecommerce.core.KafkaDispatcher;
import com.studiojms.ecommerce.core.KafkaService;
import com.studiojms.ecommerce.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author jefferson.souza
 */
public class FraudDetectorService {

    private final static String TOPIC_NEW_ORDER = "ECOMMERCE_NEW_ORDER";
    private final static String TOPIC_REJECTED_ORDER = "ECOMMERCE_ORDER_REJECTED";
    private final static String TOPIC_APPROVED_ORDER = "ECOMMERCE_ORDER_APPROVED";

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) throws InterruptedException {
        final var fraudDetectorService = new FraudDetectorService();
        try (var kafkaService = new KafkaService<>(FraudDetectorService.class.getSimpleName(), TOPIC_NEW_ORDER,
                fraudDetectorService::parse, Order.class)) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
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

        var order = record.value();
        if (isFraud(order)) {
            System.out.println("Order is a fraud: " + order);
            orderDispatcher.send(TOPIC_REJECTED_ORDER, order.getEmail(), order);
        } else {
            System.out.println("Order Approved: " + order);
            orderDispatcher.send(TOPIC_APPROVED_ORDER, order.getEmail(), order);
        }
    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }

}
