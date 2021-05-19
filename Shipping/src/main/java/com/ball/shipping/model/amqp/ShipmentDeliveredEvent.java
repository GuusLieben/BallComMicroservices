package com.ball.shipping.model.amqp;

import com.ball.shipping.model.mssql.Shipment;

import lombok.Getter;

@Getter
public class ShipmentDeliveredEvent extends Event {

    public ShipmentDeliveredEvent(Shipment shipment) {
        super("ShipmentDelivered", shipment);
    }
}
