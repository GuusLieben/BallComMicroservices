package nl.avans.infrastructure.broker.events.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BasketItemAddedSendEvent implements SendEvent {
    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private UUID productId;

    @JsonProperty
    private int amount;
}
