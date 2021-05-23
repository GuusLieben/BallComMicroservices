package nl.avans.domain.models.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.avans.domain.models.events.Event;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductDetailsViewedEvent implements Event {
    @JsonProperty
    private UUID productId;
}
