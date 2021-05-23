package nl.avans.domain.models.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nl.avans.domain.models.events.Event;

import java.util.UUID;

@Data
public class StockRemovedEvent implements Event {
    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private UUID productId;
}
