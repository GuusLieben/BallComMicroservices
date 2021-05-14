package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.shipment.ShipmentEvent;
import com.ball.shipping.model.amqp.shipment.ShipmentDeliveredEvent;
import com.ball.shipping.model.amqp.shipment.ShipmentReceivedEvent;
import com.ball.shipping.model.amqp.shipment.ShipmentRegisteredEvent;
import com.ball.shipping.model.amqp.shipment.ShipmentShippedEvent;
import com.ball.shipping.model.mssql.Shipment;
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

    @Value("${ball.rabbitmq.header.registered}")
    private String registeredHeader;
    @Value("${ball.rabbitmq.header.received}")
    private String receivedHeader;
    @Value("${ball.rabbitmq.header.shipped}")
    private String shippedHeader;
    @Value("${ball.rabbitmq.header.delivered}")
    private String deliveredHeader;

    private final RabbitTemplate template;
    private final ObjectMapper mapper;

    public RabbitMQSender(RabbitTemplate template) {
        this.template = template;
        this.mapper = new ObjectMapper();
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

    private void broadcast(ShipmentEvent event, String messageType) {
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
