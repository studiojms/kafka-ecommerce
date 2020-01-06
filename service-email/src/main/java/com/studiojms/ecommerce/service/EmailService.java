package com.studiojms.ecommerce.service;

import com.studiojms.ecommerce.core.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author jefferson.souza
 */
public class EmailService {

    private final static String TOPIC_NAME = "ECOMMERCE_SEND_EMAIL";

    public static void main(String[] args) {
        final var emailService = new EmailService();
        try (var kafkaService = new KafkaService<>(EmailService.class.getSimpleName(), TOPIC_NAME,
                emailService::parse, String.class)) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Sending email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Email was sent.");
    }

}
