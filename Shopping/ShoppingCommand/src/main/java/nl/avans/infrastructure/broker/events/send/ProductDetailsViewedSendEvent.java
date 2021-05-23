package nl.avans.infrastructure.broker.events.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductDetailsViewedSendEvent implements SendEvent {
    @JsonProperty
    private UUID productId;
}