package nl.avans.domain.models.aggregate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.avans.domain.models.events.basket.BasketEventModel;
import nl.avans.domain.models.events.basket.BasketItemAddedEvent;
import nl.avans.domain.models.events.basket.BasketItemRemovedEvent;
import nl.avans.domain.models.models.Basket;
import nl.avans.domain.models.models.BasketItem;
import nl.avans.domain.models.models.Customer;
import nl.avans.domain.models.models.Product;
import nl.avans.domain.services.aggregate.AggregateBasket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AggregateBasketEvents implements AggregateBasket {
    public Basket aggregate(ArrayList<BasketEventModel> events) {
        Basket basket = new Basket();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (BasketEventModel event : events) {
                switch (event.getEvent()) {
                    case "CustomerAdded":
                        basket.setCustomer(mapper.readValue(event.getData(), Customer.class));
                        break;
                    case "BasketItemAdded":
                        BasketItemAddedEvent basketItemAddedEvent = mapper.readValue(event.getData(), BasketItemAddedEvent.class);
                        BasketItem basketItem = new BasketItem();
                        basketItem.setAmount(basketItemAddedEvent.getAmount());
                        Product product = new Product();
                        product.setProductId(basketItemAddedEvent.getProductId());
                        basketItem.setProduct(product);
                        basket.getProducts().add(basketItem);
                        break;
                    case "BasketItemRemoved":
                        BasketItemRemovedEvent basketItemRemovedEvent = mapper.readValue(event.getData(), BasketItemRemovedEvent.class);
                        for (int index = 0; index < basket.getProducts().size(); index++) {
                            if (basket.getProducts().get(index).getProduct().getProductId().equals(basketItemRemovedEvent.getProductId())) {
                                basket.getProducts().remove(index);
                                break;
                            }
                        }
                        break;
                    case "OrderCreated":
                        basket.setProducts(new ArrayList<>());
                        break;
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return basket;
    }
}
