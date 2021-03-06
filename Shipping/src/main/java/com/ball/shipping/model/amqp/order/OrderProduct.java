package com.ball.shipping.model.amqp.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "basket")
public class OrderProduct {

    @Id
    @JsonIgnore
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private int amount;
    @OneToOne
    private Product product;

    private double weight;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonInclude(Include.NON_NULL)
    private OrderCreatedEvent event;

}
