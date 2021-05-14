package com.ball.shipping.model.amqp.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private String name;
    private String supplier;
    private String brand;
    private String barcode;
    private String amount;

}
