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
            var email = Math.random() + "@email.com";

            for (var i = 0; i < 10; i++) {
                var orderId = UUID.randomUUID().toString();
                var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                var order = new Order(orderId, amount, email);

                orderDispatcher.send(NEW_ORDER_TOPIC, email, order);

                var emailContent = "Thanks for your order. It's now in process.";
                emailDispatcher.send(SEND_EMAIL_TOPIC, email, emailContent);
            }
        }
    }


}
