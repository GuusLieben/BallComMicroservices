package com.ball.shipping.model.mssql;

import com.ball.shipping.model.filters.IdentifierPresentFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "shippers")
@JsonInclude(Include.NON_DEFAULT)
public class Shipper implements Serializable {

    @Id
    @JsonInclude(value = Include.CUSTOM, valueFilter = IdentifierPresentFilter.class)
    @GeneratedValue
    private Long shipperId;

    @JsonInclude(Include.NON_NULL)
    private String name;

    private double pricePerKm;
    private double pricePerKg;
    private double pricePerPackage;

    private double maxKm;
    private double maxKg;

    @OneToMany(mappedBy = "shipper")
    @JsonInclude(Include.NON_NULL)
    private List<Shipment> shipment;

}
