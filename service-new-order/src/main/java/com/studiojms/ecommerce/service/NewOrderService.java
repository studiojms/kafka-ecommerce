package com.studiojms.ecommerce.service;

import com.studiojms.ecommerce.core.KafkaDispatcher;
import com.studiojms.ecommerce.domain.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author jefferson.souza
 */
public class NewOrderService {

    private final static String NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER";
    private final static String SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>();
             var emailDispatcher = new KafkaDispatcher<String>()) {

            for (var i = 0; i < 10; i++) {
                var userId = UUID.randomUUID().toString();
                var orderId = UUID.randomUUID().toString();
                var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                var order = new Order(userId, orderId, amount);

                orderDispatcher.send(NEW_ORDER_TOPIC, userId, order);

                var email = "Thanks for your order. It's now in process.";
                emailDispatcher.send(SEND_EMAIL_TOPIC, userId, email);
            }
        }
    }


}
