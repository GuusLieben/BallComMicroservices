package com.ball.shipping.model.amqp.shipment;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.mssql.Shipment;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public abstract class ShipmentEvent implements Event {

    @JsonProperty
    private String name;

    @JsonProperty
    private Shipment shipment;

    protected ShipmentEvent(String name, Shipment shipment) {
        this.name = name;
        this.shipment = shipment;
    }
}
