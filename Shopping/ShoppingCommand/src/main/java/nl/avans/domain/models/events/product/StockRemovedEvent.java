package nl.avans.domain.models.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.avans.domain.models.events.Event;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockRemovedEvent implements Event {
    @JsonProperty
    private UUID productId;

    @JsonProperty
    private int amount;
}
