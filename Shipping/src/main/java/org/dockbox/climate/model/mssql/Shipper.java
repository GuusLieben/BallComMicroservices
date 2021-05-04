package org.dockbox.climate.model.mssql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.dockbox.climate.model.filters.IdentifierPresentFilter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shippers")
@JsonInclude(Include.NON_DEFAULT)
@Setter
public class Shipper {

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
