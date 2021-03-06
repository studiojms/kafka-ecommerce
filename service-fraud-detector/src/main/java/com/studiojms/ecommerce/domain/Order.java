package com.studiojms.ecommerce.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author jefferson.souza
 */
@Getter
@ToString
@AllArgsConstructor
public class Order {
    private String orderId;
    private BigDecimal amount;
    private String email;
}
