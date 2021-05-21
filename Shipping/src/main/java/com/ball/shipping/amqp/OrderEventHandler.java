package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.order.OrderCreatedEvent;
import com.ball.shipping.repository.OrderEventRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderEventHandler implements Handler {

    private final OrderEventRepository repository;

    public OrderEventHandler(OrderEventRepository repository) {
        this.repository = repository;
    }

    public void handle(OrderCreatedEvent event) {
        event.setWritten(LocalDateTime.now());
        this.repository.save(event);
    }
}
