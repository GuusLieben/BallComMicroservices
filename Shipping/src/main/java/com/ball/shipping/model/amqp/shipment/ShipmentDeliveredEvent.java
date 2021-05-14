package com.ball.shipping.model.amqp.shipment;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentDeliveredEvent extends ShipmentEvent {

    public ShipmentDeliveredEvent(Shipment shipment) {
        super("ShipmentDelivered", shipment);
    }
}
