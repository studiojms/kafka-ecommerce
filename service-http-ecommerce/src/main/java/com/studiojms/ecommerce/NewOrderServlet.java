package com.studiojms.ecommerce;

import com.studiojms.ecommerce.core.KafkaDispatcher;
import com.studiojms.ecommerce.domain.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author jefferson.souza
 */
public class NewOrderServlet extends HttpServlet {

    private final static String NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER";
    private final static String SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL";
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var orderDispatcher = new KafkaDispatcher<Order>();
             var emailDispatcher = new KafkaDispatcher<String>()) {

            final String emailParam = req.getParameter("email");
            final String amountParam = req.getParameter("amount");

            var email = emailParam != null ? emailParam : Math.random() + "@email.com";
            var amount = amountParam != null ? new BigDecimal(amountParam) : BigDecimal.valueOf(Math.random() * 5000 + 1);

            var orderId = UUID.randomUUID().toString();

            var order = new Order(orderId, amount, email);

            orderDispatcher.send(NEW_ORDER_TOPIC, email, order);

            var emailContent = "Thanks for your order. It's now in process.";
            emailDispatcher.send(SEND_EMAIL_TOPIC, email, emailContent);

            System.out.println("New order was successfully sent");

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            resp.getWriter().println("New order sent");
        } catch (InterruptedException | ExecutionException e) {
            throw new ServletException(e);
        }

    }
}
