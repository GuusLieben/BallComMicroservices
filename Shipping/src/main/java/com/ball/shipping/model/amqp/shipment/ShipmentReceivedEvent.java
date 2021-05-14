package com.ball.shipping.model.amqp.shipment;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentReceivedEvent extends ShipmentEvent {

    public ShipmentReceivedEvent(Shipment shipment) {
        super("ShipmentReceived", shipment);
    }
}
