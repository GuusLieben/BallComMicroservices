package com.ball.shipping.model.amqp.shipment;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentRegisteredEvent extends ShipmentEvent {

    public ShipmentRegisteredEvent(Shipment shipment) {
        super("ShipmentRegistered", shipment);
    }
}
