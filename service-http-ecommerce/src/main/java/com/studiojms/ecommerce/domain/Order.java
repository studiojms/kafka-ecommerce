package com.studiojms.ecommerce.domain;

import java.math.BigDecimal;

/**
 * @author jefferson.souza
 */
public class Order {
    private String orderId;
    private BigDecimal amount;
    private String email;

    public Order(String orderId, BigDecimal amount, String email) {
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }
}
