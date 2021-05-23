package nl.avans.infrastructure.broker.events.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.models.BasketItem;
import nl.avans.domain.models.models.Order;
import nl.avans.domain.models.models.PaymentType;

import java.util.ArrayList;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class OrderCreatedSendEvent implements SendEvent {
    @JsonProperty
    private UUID orderId;

    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private ArrayList<BasketItem> basket;

    @JsonProperty
    private PaymentType paymentType;

    @JsonProperty
    private String street;

    @JsonProperty
    private String city;

    @JsonProperty
    private String postalCode;

    @JsonProperty
    private int houseNumber;

    @JsonProperty
    private String addition;

    public OrderCreatedSendEvent(Order order) {
        this.orderId = UUID.randomUUID();
        this.customerId = order.getCustomerId();
        this.paymentType = order.getPaymentType();
        this.street = order.getStreet();
        this.city = order.getCity();
        this.postalCode = order.getPostalCode();
        this.houseNumber = order.getHouseNumber();
        this.addition = order.getAddition();
        this.basket = order.getProducts();
    }
}
