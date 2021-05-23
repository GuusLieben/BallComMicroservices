package nl.avans.domain.models.aggregate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.models.events.product.StockAddedEvent;
import nl.avans.domain.models.events.product.StockRemovedEvent;
import nl.avans.domain.models.models.Product;
import nl.avans.domain.services.aggregate.AggregateProduct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AggregateProductEvents implements AggregateProduct {
    @Override
    public Product aggregate(ArrayList<ProductEventModel> events) {
        Product product = new Product();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (ProductEventModel event : events) {
                switch (event.getEvent()) {
                    case "ProductCreated":
                        product = mapper.readValue(event.getData(), Product.class);
                        break;
                    case "ProductDetailsViewed":
                        product.setDetailsViewed(product.getDetailsViewed() + 1);
                        break;
                    case "StockAdded":
                        StockAddedEvent stockAddedEvent = mapper.readValue(event.getData(), StockAddedEvent.class);
                        product.setAmount(product.getAmount() + stockAddedEvent.getAmount());
                        break;
                    case "StockRemoved":
                        StockRemovedEvent stockRemovedEvent = mapper.readValue(event.getData(), StockRemovedEvent.class);
                        product.setAmount(product.getAmount() - stockRemovedEvent.getAmount());
                        break;
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return product;
    }
}
