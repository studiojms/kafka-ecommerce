package com.studiojms.ecommerce.domain;

import java.math.BigDecimal;

/**
 * @author jefferson.souza
 */
public class Order {
    private String userId;
    private String orderId;
    private BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
