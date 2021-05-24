package nl.avans.domain.models.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.avans.domain.models.events.Event;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketItemRemovedEvent implements Event {
    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private UUID productId;
}
