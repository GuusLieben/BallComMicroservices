package nl.avans.infrastructure.api.models.crud;

import nl.avans.domain.models.Basket;
import nl.avans.infrastructure.api.models.viewmodels.AddOrDeleteProductToBasketViewModel;

import java.util.UUID;

public interface BasketCRUDViewModel {
    Basket get(UUID customerId);
    void addProduct(AddOrDeleteProductToBasketViewModel basket);
    void removeProduct(Basket basket);
}
