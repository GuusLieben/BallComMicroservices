package com.ball.shipping.model.amqp;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.ball.shipping.model.mssql.Shipment;

import java.io.Serializable;

import lombok.Getter;

@Getter
public abstract class Event implements Serializable {

    @JsonProperty
    private String name;

    @JsonProperty
    private Shipment shipment;

    protected Event(String name, Shipment shipment) {
        this.name = name;
        this.shipment = shipment;
    }
}
