package com.ball.shipping.model.amqp.shipment;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentShippedEvent extends ShipmentEvent {

    public ShipmentShippedEvent(Shipment shipment) {
        super("ShipmentShipped", shipment);
    }
}
