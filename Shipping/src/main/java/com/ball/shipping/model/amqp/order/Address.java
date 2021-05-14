package com.ball.shipping.model.amqp.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Address {

    private String city;
    private String street;
    private short houseNumber;
    private String addition;
    private String postalCode;

}
