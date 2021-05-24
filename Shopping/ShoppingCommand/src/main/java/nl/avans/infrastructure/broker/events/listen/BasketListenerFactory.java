package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.basket.BasketEventModel;
import nl.avans.domain.models.events.basket.CustomerAddedEvent;
import nl.avans.domain.models.models.BasketItem;
import nl.avans.domain.services.repository.BasketRepository;

@Service
@RequiredArgsConstructor
public class BasketListenerFactory implements BasketListener {
    private final BasketRepository basketRepository;

    @Override
    public void execute(String type, String payload) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            Set<String> events = Set.of("CustomerAdded");
            if (events.contains(type)) {
                BasketEventModel basketEventModel = new BasketEventModel();
                basketEventModel.setEvent(type);
                CustomerAddedEvent customerAddedEvent = mapper.readValue(payload, CustomerAddedEvent.class);
                customerAddedEvent.setProducts(new ArrayList<BasketItem>());
                basketEventModel.setCustomerId(customerAddedEvent.getGuid());
                basketEventModel.setData(mapper.writeValueAsString(customerAddedEvent));
                basketRepository.create(basketEventModel);
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error execute BasketListenerFactory");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
