package com.ball.shipping.model.common;

import com.ball.shipping.amqp.RabbitMQSender;
import com.ball.shipping.model.mssql.Shipment;

import java.util.function.BiConsumer;

public enum ShipmentState {
    REGISTERED(RabbitMQSender::shipmentRegistered),
    RECEIVED(RabbitMQSender::shipmentReceived),
    SHIPPED(RabbitMQSender::shipmentShipped),
    DELIVERED(RabbitMQSender::shipmentDelivered);

    private final BiConsumer<RabbitMQSender, Shipment> action;

    ShipmentState(BiConsumer<RabbitMQSender, Shipment> action) {
        this.action = action;
    }

    public void perform(RabbitMQSender sender, Shipment shipment) {
        this.action.accept(sender, shipment);
    }
}
