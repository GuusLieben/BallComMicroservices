package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.ShipmentDeliveredEvent;
import com.ball.shipping.model.amqp.ShipmentReceivedEvent;
import com.ball.shipping.model.amqp.ShipmentRegisteredEvent;
import com.ball.shipping.model.amqp.ShipmentShippedEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "false"),
            exchange = @Exchange("${ball.rabbitmq.registered}"),
            key = "${ball.rabbitmq.queue}"
    ))
    public void whenShipmentRegistered(ShipmentRegisteredEvent event) {
        System.out.println("Registered shipment for: " + event.getShipment().getFirstName() + event.getShipment().getLastName());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "false"),
            exchange = @Exchange("${ball.rabbitmq.received}"),
            key = "${ball.rabbitmq.queue}"
    ))
    public void whenShipmentReceived(ShipmentReceivedEvent event) {
        System.out.println("Received shipment for: " + event.getShipment().getFirstName() + event.getShipment().getLastName());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "false"),
            exchange = @Exchange("${ball.rabbitmq.shipped}"),
            key = "${ball.rabbitmq.queue}"
    ))
    public void whenShipmentShipped(ShipmentShippedEvent event) {
        System.out.println("Shipped shipment for: " + event.getShipment().getFirstName() + event.getShipment().getLastName());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "false"),
            exchange = @Exchange("${ball.rabbitmq.delivered}"),
            key = "${ball.rabbitmq.queue}"
    ))
    public void whenShipmentDelivered(ShipmentDeliveredEvent event) {
        System.out.println("Delivered shipment for: " + event.getShipment().getFirstName() + event.getShipment().getLastName());
    }
}
