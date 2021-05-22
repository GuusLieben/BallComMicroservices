package nl.avans.infrastructure.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.avans.infrastructure.broker.events.*;
import nl.avans.infrastructure.broker.events.basket.BasketItemAddedListenEvent;
import nl.avans.infrastructure.broker.events.basket.BasketItemRemovedListenEvent;
import nl.avans.infrastructure.broker.events.basket.CustomerAddedQueryListenEvent;
import nl.avans.infrastructure.broker.events.basket.OrderCreatedListenEvent;
import nl.avans.infrastructure.broker.events.product.ProductCreatedListenEvent;
import nl.avans.infrastructure.broker.events.product.ProductDetailsViewedListenEvent;
import nl.avans.infrastructure.broker.events.product.StockAddedListenEvent;
import nl.avans.infrastructure.broker.events.product.StockRemovedListenEvent;

public class ListenEventFactory {
    public static void execute(String type, String listenEvent) {
        ListenEvent event = null;
        ObjectMapper mapper = new ObjectMapper();

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
                    event = mapper.readValue(listenEvent, ProductDetailsViewedListenEvent.class);
                    break;
                case "StockAdded" :
                    event = mapper.readValue(listenEvent, StockAddedListenEvent.class);
                    break;
                case "StockRemoved" :
                    event = mapper.readValue(listenEvent, StockRemovedListenEvent.class);
                    break;
                case "ProductCreated" :
                    event = mapper.readValue(listenEvent, ProductCreatedListenEvent.class);
                    break;
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        if (event != null) {
            event.execute();
        }
    }
}
