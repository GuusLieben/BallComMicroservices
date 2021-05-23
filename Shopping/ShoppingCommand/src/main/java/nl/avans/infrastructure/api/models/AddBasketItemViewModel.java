package nl.avans.infrastructure.api.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class AddBasketItemViewModel {
    private UUID customerId;
    private UUID productId;
    private int amount;
}
