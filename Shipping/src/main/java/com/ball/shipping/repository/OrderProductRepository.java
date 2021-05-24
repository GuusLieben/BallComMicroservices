package com.ball.shipping.repository;

import com.ball.shipping.model.amqp.order.OrderProduct;

import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, Long> {
}
