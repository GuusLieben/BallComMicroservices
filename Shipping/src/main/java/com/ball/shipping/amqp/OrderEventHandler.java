package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.order.OrderCreatedEvent;
import com.ball.shipping.model.amqp.order.OrderProduct;
import com.ball.shipping.repository.OrderEventRepository;
import com.ball.shipping.repository.OrderProductRepository;
import com.ball.shipping.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderEventHandler implements Handler {

    private final OrderEventRepository orderEventRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    public void handle(OrderCreatedEvent event) {
        event.setWritten(LocalDateTime.now());
        for (OrderProduct orderProduct : event.getBasket()) {
            this.productRepository.save(orderProduct.getProduct());
            this.orderProductRepository.save(orderProduct);
        }
        this.orderEventRepository.save(event);
    }
}
