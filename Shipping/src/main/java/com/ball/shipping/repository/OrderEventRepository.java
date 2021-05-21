package com.ball.shipping.repository;

import com.ball.shipping.model.amqp.order.OrderCreatedEvent;

import org.springframework.data.repository.CrudRepository;

public interface OrderEventRepository extends CrudRepository<OrderCreatedEvent, Long> {
}
