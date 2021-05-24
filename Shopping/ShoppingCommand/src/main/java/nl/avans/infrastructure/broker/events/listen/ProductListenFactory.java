package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.product.ProductCreatedEvent;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.models.events.product.StockAddedEvent;
import nl.avans.domain.models.events.product.StockRemovedEvent;
import nl.avans.domain.models.models.Product;
import nl.avans.domain.services.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductListenFactory implements ProductListener {
    private final ProductRepository productRepository;

    @Override
    public void execute(String type, String payload) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            Set<String> events = Set.of("ProductCreated", "StockAdded", "StockRemoved");
            if (events.contains(type)) {
                ProductEventModel productEventModel = new ProductEventModel();
                productEventModel.setEvent(type);
                switch (type) {
                    case "ProductCreated":
                        ProductCreatedEvent productCreatedEvent = mapper.readValue(payload, ProductCreatedEvent.class);
                        productEventModel.setProductId(productCreatedEvent.getProductId());
                        productEventModel.setData(mapper.writeValueAsString(productCreatedEvent));
                        break;
                    case "StockAdded":
                        StockAddedEvent stockAddedEvent = mapper.readValue(payload, StockAddedEvent.class);
                        productEventModel.setProductId(stockAddedEvent.getProductId());
                        productEventModel.setData(mapper.writeValueAsString(stockAddedEvent));
                        break;
                    case "StockRemoved":
                        StockRemovedEvent stockRemovedEvent = mapper.readValue(payload, StockRemovedEvent.class);
                        productEventModel.setProductId(stockRemovedEvent.getProductId());
                        productEventModel.setData(mapper.writeValueAsString(stockRemovedEvent));
                        break;
                }
                productRepository.create(productEventModel);
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error ProductListenFactory BasketListenerFactory");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
