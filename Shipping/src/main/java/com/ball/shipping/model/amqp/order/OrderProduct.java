package com.ball.shipping.model.amqp.order;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderProduct {

    private UUID productId;
    private int amount;

}
