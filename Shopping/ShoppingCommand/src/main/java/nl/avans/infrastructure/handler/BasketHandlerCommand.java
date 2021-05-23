package nl.avans.infrastructure.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.basket.BasketEventModel;
import nl.avans.domain.models.events.basket.BasketItemAddedEvent;
import nl.avans.domain.models.events.basket.BasketItemRemovedEvent;
import nl.avans.domain.models.events.basket.OrderCreatedEvent;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.models.models.Basket;
import nl.avans.domain.models.models.BasketItem;
import nl.avans.domain.models.models.Order;
import nl.avans.domain.models.models.Product;
import nl.avans.domain.services.aggregate.AggregateBasket;
import nl.avans.domain.services.aggregate.AggregateProduct;
import nl.avans.domain.services.handler.BasketHandler;
import nl.avans.domain.services.message.BrokerMessageSender;
import nl.avans.domain.services.repository.BasketRepository;
import nl.avans.domain.services.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketHandlerCommand implements BasketHandler {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BrokerMessageSender brokerMessageSender;
    private final AggregateBasket aggregateBasket;
    private final AggregateProduct aggregateProduct;

    @Override
    public void addBasketItem(UUID customerId, UUID productId, int amount) {
        ArrayList<BasketEventModel> events = basketRepository.getById(customerId);
        Basket basket = aggregateBasket.aggregate(events);

        if (basket.getProducts().size() <= 20) {
            BasketEventModel basketEventModel = new BasketEventModel();
            basketEventModel.setCustomerId(customerId);
            basketEventModel.setEvent("BasketItemAdded");

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                basketEventModel.setData(mapper.writeValueAsString(new BasketItemAddedEvent(customerId, productId, amount)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            basketRepository.create(basketEventModel);
            brokerMessageSender.basketItemAdded(new BasketItemAddedEvent(customerId, productId, amount));
        }
    }

    @Override
    public void removeBasketItem(UUID customerId, UUID productId) {
        BasketEventModel basketEventModel = new BasketEventModel();
        basketEventModel.setCustomerId(customerId);
        basketEventModel.setEvent("BasketItemRemoved");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            basketEventModel.setData(mapper.writeValueAsString(new BasketItemRemovedEvent(customerId, productId)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        basketRepository.create(basketEventModel);
        brokerMessageSender.basketItemRemoved(new BasketItemRemovedEvent(customerId, productId));
    }

    @Override
    public void checkout(Order order) {
        ArrayList<BasketEventModel> basketEvents = basketRepository.getById(order.getCustomerId());
        Basket basket = aggregateBasket.aggregate(basketEvents);

        ArrayList<BasketItem> basketItems = new ArrayList<>();
        for(BasketItem basketItem : basket.getProducts()) {
            ArrayList<ProductEventModel> productEvents = productRepository.getById(basketItem.getProduct().getProductId());
            Product product = aggregateProduct.aggregate(productEvents);
            BasketItem searchBasketItem = new BasketItem();
            searchBasketItem.setProduct(product);
            searchBasketItem.setAmount(basketItem.getAmount());
            basketItems.add(searchBasketItem);
        }
        order.setProducts(basketItems);

        BasketEventModel basketEventModel = new BasketEventModel();
        basketEventModel.setCustomerId(order.getCustomerId());
        basketEventModel.setEvent("OrderCreated");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                UUID.randomUUID(), order.getCustomerId(), order.getPaymentType(), order.getStreet(), order.getCity(),
                order.getPostalCode(), order.getHouseNumber(), order.getAddition(), order.getProducts());
        try {
            basketEventModel.setData(mapper.writeValueAsString(orderCreatedEvent));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        basketRepository.create(basketEventModel);
        brokerMessageSender.orderCreated(orderCreatedEvent);
    }
}
