package com.ball.shipping.model.amqp.order;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.mssql.Shipment;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreatedEvent implements Event {

    private UUID orderId;
    private UUID customerId;
    private PaymentType paymentType;

    private String city;
    private String street;
    private short houseNumber;
    private String addition;
    private String postalCode;

    private String firstName;
    private String lastName;

    private List<OrderProduct> basket;

    public Shipment toShipment() {
        Shipment shipment = new Shipment();
        shipment.setFirstName(this.getFirstName());
        shipment.setLastName(this.getLastName());
        shipment.setWeightInKg(new Random().nextInt());
        shipment.setCity(this.getCity());
        shipment.setStreet(this.getStreet());
        shipment.setHouseNumber(this.getHouseNumber());
        shipment.setAddition(this.getAddition());
        shipment.setPostalCode(this.getPostalCode());
        return shipment;
    }
}
