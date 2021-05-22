package nl.avans.infrastructure.api.models.crud;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.BasketItem;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.api.models.viewmodels.AddOrDeleteProductToBasketViewModel;
import nl.avans.infrastructure.broker.BrokerMessageSender;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;
import nl.avans.infrastructure.repositories.read_db.RepositoryReadDBFactory;

import java.util.UUID;

@RequiredArgsConstructor
public class BasketViewModelCQRS implements BasketCRUDViewModel {
    private final BrokerMessageSender brokerMessageSender;

    private BasketRepository repositoryRead = new RepositoryReadDBFactory().createBasketRepository();
    private BasketRepository repositoryWrite = new RepositoryReadDBFactory().createBasketRepository();

    private ProductRepository productRepository = new RepositoryReadDBFactory().createProductRepository();

    @Override
    public Basket get(UUID customerId) {
        return repositoryRead.get(customerId);
    }

    @Override
    public void addProduct(AddOrDeleteProductToBasketViewModel viewModel) {
        Product product = productRepository.getById(viewModel.getProductId());
        Basket basket = repositoryRead.get(viewModel.getCustomerId());
        BasketItem basketItem = new BasketItem(product, viewModel.getAmount());
        basket.getProducts().add(basketItem);
        basket.setEvent("Basket items added");
        brokerMessageSender.basketItemAdded(basketItem, viewModel.getCustomerId());
    }

    @Override
    public void removeProduct(Basket basket) {

    }
}
