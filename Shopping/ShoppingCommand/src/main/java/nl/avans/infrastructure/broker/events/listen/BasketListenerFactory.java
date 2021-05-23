package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.basket.BasketEventModel;
import nl.avans.domain.models.events.basket.CustomerAddedEvent;
import nl.avans.domain.services.repository.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BasketListenerFactory implements BasketListener {
    private final BasketRepository basketRepository;

    @Override
    public void execute(String type, String payload) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            Set<String> events = Set.of("CustomerAdded");
            if (events.contains(type)) {
                BasketEventModel basketEventModel = new BasketEventModel();
                basketEventModel.setEvent(type);
                basketEventModel.setCustomerId(mapper.readValue(payload, CustomerAddedEvent.class).getCustomerId());
                basketEventModel.setData(payload);
                basketRepository.create(basketEventModel);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
