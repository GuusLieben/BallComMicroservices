package com.ball.shipping.model.mssql;

import com.ball.shipping.model.common.ShipmentState;
import com.ball.shipping.model.filters.IdentifierPresentFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "shipments")
@JsonInclude(Include.NON_DEFAULT)
public class Shipment implements Serializable {

    @Id
    @JsonInclude(value = Include.CUSTOM, valueFilter = IdentifierPresentFilter.class)
    @GeneratedValue
    private Long shipmentId;
    private String firstName;
    private String lastName;

    private String city;
    private String street;
    private short houseNumber;

    @Column(nullable = true)
    private String addition;
    private String postalCode;

    private double weightInKg;
    private ShipmentState state;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    @JsonInclude(Include.NON_NULL)
    private Shipper shipper;

}
