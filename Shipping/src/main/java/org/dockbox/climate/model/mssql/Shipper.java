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
@Table(name = "shippers")
public class Shipper {

    @Id
    @JsonIgnore
    private Long userId;

    private String name;

    private double pricePerKm;
    private double pricePerKg;
    private double pricePerPackage;

    private double maxKm;
    private double maxKg;

}
