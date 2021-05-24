package com.ball.shipping.model.amqp.order;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;
    private String name;
    private String description;
    private String supplier;
    private String brand;
    private double price;
    private double weight;

}
