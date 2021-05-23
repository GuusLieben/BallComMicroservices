package com.ball.shipping.model.amqp.order;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.mssql.Shipment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
@Entity
public class OrderCreatedEvent implements Event {

    @Id
    @JsonIgnore
    @GeneratedValue
    @Column(name = "id")
    private Long eventId;
    private LocalDateTime written;

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

    @OneToMany(mappedBy = "event")
    @JsonInclude(Include.NON_NULL)
    private List<OrderProduct> basket;

    public Shipment toShipment() {
        Shipment shipment = new Shipment();
        shipment.setFirstName(this.getFirstName());
        shipment.setLastName(this.getLastName());
        final double weight = this.basket.stream().mapToDouble(OrderProduct::getWeight).sum();
        shipment.setWeightInKg(weight);
        shipment.setCity(this.getCity());
        shipment.setStreet(this.getStreet());
        shipment.setHouseNumber(this.getHouseNumber());
        shipment.setAddition(this.getAddition());
        shipment.setPostalCode(this.getPostalCode());
        return shipment;
    }
}
