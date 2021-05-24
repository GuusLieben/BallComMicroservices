package nl.avans.domain.models.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nl.avans.domain.models.events.Event;
import nl.avans.domain.models.models.BasketItem;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class CustomerAddedEvent implements Event {
    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private ArrayList<BasketItem> products;
}
