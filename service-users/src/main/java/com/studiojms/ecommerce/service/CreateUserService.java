package com.studiojms.ecommerce.service;

import com.studiojms.ecommerce.core.KafkaService;
import com.studiojms.ecommerce.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author jefferson.souza
 */
public class CreateUserService {
    private final static String TOPIC_NEW_ORDER = "ECOMMERCE_NEW_ORDER";
    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:build/users_db.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("CREATE TABLE users " +
                    "(UUID VARCHAR(200) PRIMARY KEY, " +
                    " EMAIL VARCHAR(200))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        final var createUserService = new CreateUserService();
        try (var kafkaService = new KafkaService<>(CreateUserService.class.getSimpleName(), TOPIC_NEW_ORDER,
                createUserService::parse, Order.class)) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("--------------------------------------------------------");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value());

        var order = record.value();

        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        }

    }

    private void insertNewUser(String email) throws SQLException {
        final var statement = connection.prepareStatement("INSERT INTO users (UUID, EMAIL) " +
                " VALUES (?, ?)");

        statement.setString(1, UUID.randomUUID().toString());
        statement.setString(2, email);
        statement.execute();

        System.out.println("User added: uuid, " + email);
    }

    private boolean isNewUser(String email) throws SQLException {
        final var statement = connection.prepareStatement("SELECT uuid FROM users WHERE email = ? LIMIT 1");
        statement.setString(1, email);
        var results = statement.executeQuery();
        return !results.next();
    }


}
