package com.ball.shipping.repository;

import com.ball.shipping.model.amqp.order.Product;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
