package org.dockbox.climate.model.mssql;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shipments")
public class Shipment {

    @Id
    @JsonIgnore
    private Long shipmentId;
    private String firstName;
    private String lastName;

    private String city;
    private String street;
    private short houseNumber;
    private char addition;
    private String postalCode;

    private double weightInKg;
    private String state;

}
