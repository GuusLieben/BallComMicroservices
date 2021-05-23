package nl.avans.domain.models.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nl.avans.domain.models.events.Event;

import java.util.UUID;

@Data
public class CustomerAddedEvent implements Event {
    @JsonProperty
    private UUID customerId;
}
