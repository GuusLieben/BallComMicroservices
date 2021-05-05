package com.ball.auth.amqp;

import com.ball.auth.model.User;
import com.ball.auth.model.amqp.Event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSender {

    private final RabbitTemplate template;
    @Value("${ball.rabbitmq.exchange}")
    private String exchangeKey;
    @Value("${ball.rabbitmq.queue}")
    private String queueKey;
    @Value("${ball.rabbitmq.header.customer.created}")
    private String customerCreated;
    @Value("${ball.rabbitmq.header.customer.updated}")
    private String customerUpdated;
    @Value("${ball.rabbitmq.header.customer.deleted}")
    private String customerDeleted;
    @Value("${ball.rabbitmq.header.employee.created}")
    private String employeeCreated;
    @Value("${ball.rabbitmq.header.employee.updated}")
    private String employeeUpdated;
    @Value("${ball.rabbitmq.header.employee.deleted}")
    private String employeeDeleted;
    @Value("${ball.rabbitmq.header.supplier.created}")
    private String supplierCreated;
    @Value("${ball.rabbitmq.header.supplier.updated}")
    private String supplierUpdated;
    @Value("${ball.rabbitmq.header.supplier.deleted}")
    private String supplierDeleted;

    public RabbitMQSender(RabbitTemplate template) {
        this.template = template;
    }

    public void userCreated(User user) {
        String header;
        switch (user.getRole()) {
            case CUSTOMER:
                header = this.customerCreated;
                break;
            case SUPPLIER:
                header = this.supplierCreated;
                break;
            case EMPLOYEE:
                header = this.employeeCreated;
                break;
            default:
                throw new RuntimeException("Unsupported role: " + user.getRole());
        }
        this.broadcast(new Event.Created(header, user), header);
    }

    public void userUpdated(User user) {
        String header;
        switch (user.getRole()) {
            case CUSTOMER:
                header = this.customerUpdated;
                break;
            case SUPPLIER:
                header = this.supplierUpdated;
                break;
            case EMPLOYEE:
                header = this.employeeUpdated;
                break;
            default:
                throw new RuntimeException("Unsupported role: " + user.getRole());
        }
        this.broadcast(new Event.Updated(header, user), header);
    }

    public void userDeleted(User user) {
        String header;
        switch (user.getRole()) {
            case CUSTOMER:
                header = this.customerDeleted;
                break;
            case SUPPLIER:
                header = this.supplierDeleted;
                break;
            case EMPLOYEE:
                header = this.employeeDeleted;
                break;
            default:
                throw new RuntimeException("Unsupported role: " + user.getRole());
        }
        this.broadcast(new Event.Deleted(header, user), header);
    }

    private void broadcast(Event event, String messageType) {
        this.template.convertAndSend(this.exchangeKey, this.queueKey, event, m -> {
            m.getMessageProperties().setHeader("MessageType", messageType);
            return m;
        });
    }
}
