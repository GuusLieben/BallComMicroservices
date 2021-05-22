package com.ball.shipping.model.rest;


import com.ball.shipping.model.common.ShipmentState;
import com.ball.shipping.model.filters.IdentifierPresentFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class ShipmentUpdate {

    @JsonInclude(value = Include.CUSTOM, valueFilter = IdentifierPresentFilter.class)
    private Long shipmentId;
    private ShipmentState state;

}
