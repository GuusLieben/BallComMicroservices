package com.ball.shipping.model.amqp;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentRegisteredEvent extends Event {

    public ShipmentRegisteredEvent(Shipment shipment) {
        super("ShipmentRegistered", shipment);
    }
}
