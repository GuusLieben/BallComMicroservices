package com.ball.shipping.amqp;

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

    @Value("${ball.rabbitmq.queue}")
    private String queueKey;

    @Value("${ball.rabbitmq.registered}")
    private String registeredKey;
    @Value("${ball.rabbitmq.received}")
    private String receivedKey;
    @Value("${ball.rabbitmq.shipped}")
    private String shippedKey;
    @Value("${ball.rabbitmq.delivered}")
    private String deliveredKey;

    private final RabbitTemplate template;

    public RabbitMQSender(RabbitTemplate template) {
        this.template = template;
    }

    public void shipmentDelivered(Shipment shipment) {
        this.template.convertAndSend(this.deliveredKey, this.queueKey, new ShipmentDeliveredEvent(shipment));
    }

    public void shipmentReceived(Shipment shipment) {
        this.template.convertAndSend(this.receivedKey, this.queueKey, new ShipmentReceivedEvent(shipment));
    }

    public void shipmentRegistered(Shipment shipment) {
        this.template.convertAndSend(this.registeredKey, this.queueKey, new ShipmentRegisteredEvent(shipment));
    }

    public void shipmentShipped(Shipment shipment) {
        this.template.convertAndSend(this.shippedKey, this.queueKey, new ShipmentShippedEvent(shipment));
    }

}
