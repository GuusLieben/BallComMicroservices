package nl.avans.infrastructure.broker.events.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nl.avans.domain.models.BasketItem;

import java.util.UUID;

@Data
public class BasketItemAddedSendEvent implements SendEvent {
    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private UUID productId;

    @JsonProperty
    private int amount;

    public BasketItemAddedSendEvent(BasketItem basketItem, UUID customerId) {
        this.productId = basketItem.getProduct().getProductId();
        this.customerId = customerId;
        this.amount = basketItem.getAmount();
    }
}
