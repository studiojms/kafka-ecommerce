package com.studiojms.ecommerce.function;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author jefferson.souza
 */
public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> record);
}
