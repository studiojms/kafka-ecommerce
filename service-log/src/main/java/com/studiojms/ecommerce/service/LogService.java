package com.studiojms.ecommerce.service;

import com.studiojms.ecommerce.core.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author jefferson.souza
 */
public class LogService {

    private final static String TOPIC_PATTERN = "ECOMMERCE.*";

    public static void main(String[] args) {
        var logService = new LogService();
        var kafkaService = new KafkaService<>(LogService.class.getSimpleName(),
                Pattern.compile(TOPIC_PATTERN),
                logService::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
        kafkaService.run();
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("--------------------------------------------------------");
        System.out.println("LOG: " + record.topic());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }

}
