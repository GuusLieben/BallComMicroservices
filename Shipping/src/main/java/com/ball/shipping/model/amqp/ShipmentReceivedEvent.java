package com.ball.shipping.model.amqp;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentReceivedEvent extends Event {

    public ShipmentReceivedEvent(Shipment shipment) {
        super("ShipmentReceived", shipment);
    }
}
