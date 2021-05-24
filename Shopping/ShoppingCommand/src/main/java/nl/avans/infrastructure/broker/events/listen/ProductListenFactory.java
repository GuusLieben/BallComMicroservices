package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.product.ProductCreatedEvent;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.models.events.product.StockAddedEvent;
import nl.avans.domain.models.events.product.StockRemovedEvent;
import nl.avans.domain.services.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductListenFactory implements ProductListener {
    private final ProductRepository productRepository;

    @Override
    public void execute(String type, String payload) {
        System.out.println("EXECUTING");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            Set<String> events = Set.of("ProductCreated", "StockAdded", "StockRemoved");
            if (events.contains(type)) {
                System.out.println("EVENT");
                ProductEventModel productEventModel = new ProductEventModel();
                productEventModel.setEvent(type);
                switch (type) {
                    case "ProductCreated":
                        System.out.println("PCREATED");
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
