package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.amqp.ShipmentDeliveredEvent;
import com.ball.shipping.model.amqp.ShipmentReceivedEvent;
import com.ball.shipping.model.amqp.ShipmentRegisteredEvent;
import com.ball.shipping.model.amqp.ShipmentShippedEvent;
import com.ball.shipping.model.mssql.Shipment;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSender {

    @Value("${ball.rabbitmq.exchange}")
    private String exchangeKey;
    @Value("${ball.rabbitmq.queue}")
    private String queueKey;

    @Value("${ball.rabbitmq.header.registered}")
    private String registeredHeader;
    @Value("${ball.rabbitmq.header.received}")
    private String receivedHeader;
    @Value("${ball.rabbitmq.header.shipped}")
    private String shippedHeader;
    @Value("${ball.rabbitmq.header.delivered}")
    private String deliveredHeader;

    private final RabbitTemplate template;

    public RabbitMQSender(RabbitTemplate template) {
        this.template = template;
    }

    public void shipmentDelivered(Shipment shipment) {
        this.broadcast(new ShipmentDeliveredEvent(shipment), this.deliveredHeader);
    }

    public void shipmentReceived(Shipment shipment) {
        this.broadcast(new ShipmentReceivedEvent(shipment), this.receivedHeader);
    }

    public void shipmentRegistered(Shipment shipment) {
        this.broadcast(new ShipmentRegisteredEvent(shipment), this.registeredHeader);
    }

    public void shipmentShipped(Shipment shipment) {
        this.broadcast(new ShipmentShippedEvent(shipment), this.shippedHeader);
    }

    private void broadcast(Event event, String messageType) {
        this.template.convertAndSend(this.exchangeKey, this.queueKey, event, m -> {
            m.getMessageProperties().setHeader("MessageType", messageType);
            return m;
        });
    }
}
