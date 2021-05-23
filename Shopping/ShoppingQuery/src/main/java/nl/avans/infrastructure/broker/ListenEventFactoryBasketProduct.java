package nl.avans.infrastructure.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.broker.events.basket.*;
import nl.avans.infrastructure.broker.events.product.ProductCreatedProductListenEvent;
import nl.avans.infrastructure.broker.events.product.ProductDetailsViewedProductListenEvent;
import nl.avans.infrastructure.broker.events.product.StockAddedProductListenEvent;
import nl.avans.infrastructure.broker.events.product.StockRemovedProductListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenEventFactoryBasketProduct implements ListenEventFactory {
    private final ProductRepository productRepository;
    private final BasketRepository basketRepository;

    @Override
    public void execute(String type, String listenEvent) {
        ListenEvent event = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            switch (type) {
                case "CustomerAdded" :
                    event = mapper.readValue(listenEvent, CustomerAddedQueryListenEvent.class);
                    break;
                case "BasketItemAdded" :
                    event = mapper.readValue(listenEvent, BasketItemAddedListenEvent.class);
                    break;
                case "BasketItemRemoved" :
                    event = mapper.readValue(listenEvent, BasketItemRemovedListenEvent.class);
                    break;
                case "OrderCreated" :
                    event = mapper.readValue(listenEvent, OrderCreatedListenEvent.class);
                    break;
                case "ProductDetailsViewed" :
                    event = mapper.readValue(listenEvent, ProductDetailsViewedProductListenEvent.class);
                    break;
                case "StockAdded" :
                    event = mapper.readValue(listenEvent, StockAddedProductListenEvent.class);
                    break;
                case "StockRemoved" :
                    event = mapper.readValue(listenEvent, StockRemovedProductListenEvent.class);
                    break;
                case "ProductCreated" :
                    event = mapper.readValue(listenEvent, ProductCreatedProductListenEvent.class);
                    break;
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        if (event != null) {
            event.execute(productRepository, basketRepository);
        }
    }
}
