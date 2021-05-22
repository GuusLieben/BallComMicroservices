package nl.avans.infrastructure.api.models.viewmodels;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class AddOrDeleteProductToBasketViewModel {
    private UUID customerId;
    private UUID productId;
    private int amount;
}
