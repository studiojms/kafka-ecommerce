package com.studiojms.ecommerce.core;

import com.studiojms.ecommerce.serializer.GsonSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author jefferson.souza
 */
public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, T> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(getProperties());

    }

    private static Properties getProperties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());

        return properties;
    }

    private static Callback getCallback() {
        return (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("success sending data to " + data.topic() + "::: partition: " + data.partition() + "/offset: " + data.offset() + "/timestamp: " + data.timestamp());
        };
    }

    public void send(String topicName, String key, T value) throws ExecutionException, InterruptedException {
        var newOrderRecord = new ProducerRecord<>(topicName, key, value);
        producer.send(newOrderRecord, getCallback()).get();
    }

    @Override
    public void close() {
        this.producer.close();
    }
}
