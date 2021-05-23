package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.services.repository.ProductRepository;
import org.springframework.stereotype.Service;

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
                productEventModel.setProductId(mapper.readValue(payload, ProductEventModel.class).getProductId());
                productEventModel.setData(payload);
                productRepository.create(productEventModel);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
