package nl.avans.infrastructure.api.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class RemoveBasketIdemViewModel {
    private UUID customerId;
    private UUID productId;
}
