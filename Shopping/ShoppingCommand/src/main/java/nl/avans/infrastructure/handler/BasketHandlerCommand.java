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
import nl.avans.domain.models.message.ReturnObject;
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
    public ReturnObject<Basket> addBasketItem(UUID customerId, UUID productId, int amount) {
        ArrayList<BasketEventModel> events = basketRepository.getById(customerId);
        Basket basket = aggregateBasket.aggregate(events);

        if (basket.getProducts().size() > 20) {
            return new ReturnObject<Basket>("Reached the limit of 20 items. You can not add more items to you basket.", null);
        }
        for (BasketItem searchItem : basket.getProducts()) {
            if (searchItem.getProduct().getProductId().equals(productId)) {
                return new ReturnObject<>("Can not add the same item twice." +
                        " If you want to change your amount please delete the item first and add it afterwards with the adjusted amount.", null);
            }
        }
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
        return new ReturnObject<>(null, basket);
    }

    @Override
    public ReturnObject<BasketItem> removeBasketItem(UUID customerId, UUID productId) {
        ArrayList<BasketEventModel> events = basketRepository.getById(customerId);
        Basket basket = aggregateBasket.aggregate(events);
        BasketItem basketItem = null;
        for (BasketItem searchItem : basket.getProducts()) {
            if (searchItem.getProduct().getProductId().equals(productId)) {
                basketItem = searchItem;
            }
        }
        if (basketItem == null) {
            return new ReturnObject<>("The item you want to delete is currently not in your basket.", null);
        }

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
        return new ReturnObject<>(null, basketItem);
    }

    @Override
    public ReturnObject<Order> checkout(Order order) {
        ArrayList<BasketEventModel> basketEvents = basketRepository.getById(order.getCustomerId());
        Basket basket = aggregateBasket.aggregate(basketEvents);

        if (basket.getProducts() == null || basket.getProducts().size() == 0) {
            return new ReturnObject<>("There are no items in your basket.", null);
        }

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
        return new ReturnObject<>(null, order);
    }
}
