package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.ShipmentRegisteredEvent;

public class EventHandler {

    public void handle(ShipmentRegisteredEvent event) {
        System.out.println("Registered shipment!");
    }

}
