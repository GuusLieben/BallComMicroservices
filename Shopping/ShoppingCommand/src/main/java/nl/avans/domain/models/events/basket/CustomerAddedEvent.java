package nl.avans.domain.models.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Data;
import nl.avans.domain.models.events.Event;
import nl.avans.domain.models.models.BasketItem;

@Data
public class CustomerAddedEvent implements Event {
    @JsonProperty
    private UUID guid;

    @JsonProperty
    private ArrayList<BasketItem> products;
}
