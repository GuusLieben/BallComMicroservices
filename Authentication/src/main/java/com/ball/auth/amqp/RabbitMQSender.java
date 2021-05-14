package com.ball.auth.amqp;

import com.ball.auth.model.User;
import com.ball.auth.model.UserRole;
import com.ball.auth.model.amqp.AuthEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSender {

    @Value("${ball.rabbitmq.exchange}")
    private String exchangeKey;
    @Value("${ball.rabbitmq.queue}")
    private String queueKey;

    @Value("${ball.rabbitmq.header.customer}")
    private String customerHeader;
    @Value("${ball.rabbitmq.header.employee}")
    private String employeeHeader;
    @Value("${ball.rabbitmq.header.supplier}")
    private String supplierHeader;

    @Value("${ball.rabbitmq.header.added}")
    private String addedHeader;
    @Value("${ball.rabbitmq.header.updated}")
    private String updatedHeader;
    @Value("${ball.rabbitmq.header.deleted}")
    private String deletedHeader;

    private final RabbitTemplate template;
    private final ObjectMapper mapper;

    public RabbitMQSender(RabbitTemplate template) {
        this.template = template;
        this.mapper = new ObjectMapper();
    }

    public void userCreated(User user) {
        this.broadcast(new AuthEvent.Created(user), this.getRoleHeader(user.getRole()) + this.addedHeader);
    }

    public void userUpdated(User user) {
        this.broadcast(new AuthEvent.Updated(user), this.getRoleHeader(user.getRole()) + this.updatedHeader);
    }

    public void userDeleted(User user) {
        this.broadcast(new AuthEvent.Deleted(user), this.getRoleHeader(user.getRole()) + this.deletedHeader);
    }

    private String getRoleHeader(UserRole role) {
        switch (role) {
            case CUSTOMER:
                return this.customerHeader;
            case SUPPLIER:
                return this.supplierHeader;
            case EMPLOYEE:
                return this.employeeHeader;
            default:
                throw new RuntimeException("Unsupported role: " + role);
        }
    }

    private void broadcast(AuthEvent event, String messageType) {
        try {
            String eventJson = this.mapper.writeValueAsString(event);
            this.template.convertAndSend(this.exchangeKey, this.queueKey, eventJson, m -> {
                m.getMessageProperties().setHeader("MessageType", messageType);
                return m;
            });
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
