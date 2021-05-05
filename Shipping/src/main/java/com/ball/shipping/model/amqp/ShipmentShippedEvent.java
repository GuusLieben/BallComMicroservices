package com.ball.shipping.model.amqp;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentShippedEvent extends Event {

    public ShipmentShippedEvent(Shipment shipment) {
        super("ShipmentShipped", shipment);
    }
}
