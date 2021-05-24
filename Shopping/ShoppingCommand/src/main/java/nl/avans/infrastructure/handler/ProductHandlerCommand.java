package nl.avans.infrastructure.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.product.ProductDetailsViewedEvent;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.models.message.ReturnObject;
import nl.avans.domain.models.models.Product;
import nl.avans.domain.services.aggregate.AggregateProduct;
import nl.avans.domain.services.handler.ProductHandler;
import nl.avans.domain.services.message.BrokerMessageSender;
import nl.avans.domain.services.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductHandlerCommand implements ProductHandler {
    private final ProductRepository productRepository;
    private final BrokerMessageSender brokerMessageSender;
    private final AggregateProduct aggregateProduct;

    @Override
    public ReturnObject<Product> upDetailsViewed(UUID productId) {
        ArrayList<ProductEventModel> productEvents = productRepository.getById(productId);
        if (productEvents == null || productEvents.size() == 0) {
            return new ReturnObject<Product>("Product not found.", null);
        }
        Product product = aggregateProduct.aggregate(productEvents);

        ProductEventModel productEventModel = new ProductEventModel();
        productEventModel.setProductId(productId);
        productEventModel.setEvent("ProductDetailsViewed");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            productEventModel.setData(mapper.writeValueAsString(new ProductDetailsViewedEvent(productId)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        productRepository.create(productEventModel);
        brokerMessageSender.productDetailsViewed(new ProductDetailsViewedEvent(productId));
        return new ReturnObject<>(null, product);
    }
}
