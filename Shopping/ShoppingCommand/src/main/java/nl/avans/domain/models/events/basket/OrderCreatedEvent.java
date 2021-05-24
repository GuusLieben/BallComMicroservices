package nl.avans.domain.models.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.avans.domain.models.events.Event;
import nl.avans.domain.models.models.BasketItem;
import nl.avans.domain.models.models.PaymentType;

import java.util.ArrayList;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent implements Event {

    @JsonProperty
    private UUID orderId;

    @JsonProperty
    private UUID customerId;

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

    @JsonProperty
    private ArrayList<BasketItem> basket;
}
