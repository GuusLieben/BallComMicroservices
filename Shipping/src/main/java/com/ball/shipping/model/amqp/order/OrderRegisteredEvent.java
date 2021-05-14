package com.ball.shipping.model.amqp.order;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.mssql.Shipment;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRegisteredEvent implements Event {

    private String firstName;
    private String lastName;
    private double weight;
    private Address address;
    private List<Product> products;

    public Shipment toShipment() {
        Shipment shipment = new Shipment();
        shipment.setFirstName(this.getFirstName());
        shipment.setLastName(this.getLastName());
        shipment.setWeightInKg(this.getWeight());
        shipment.setCity(this.getAddress().getCity());
        shipment.setStreet(this.getAddress().getStreet());
        shipment.setHouseNumber(this.getAddress().getHouseNumber());
        shipment.setAddition(this.getAddress().getAddition());
        shipment.setPostalCode(this.getAddress().getPostalCode());
        return shipment;
    }
}
