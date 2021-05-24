package nl.avans.infrastructure.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.Event;
import nl.avans.domain.models.events.basket.BasketItemAddedEvent;
import nl.avans.domain.models.events.basket.BasketItemRemovedEvent;
import nl.avans.domain.models.events.basket.OrderCreatedEvent;
import nl.avans.domain.models.events.product.ProductDetailsViewedEvent;
import nl.avans.domain.services.message.BrokerMessageSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQSender implements BrokerMessageSender {
    @Value("${ball.rabbitmq.exchange}")
    private String exchangeKey;
    @Value("${ball.rabbitmq.queue}")
    private String queueKey;

    @Value("${ball.rabbitmq.header.order.created}")
    private String orderCreatedHeader;
    @Value("${ball.rabbitmq.header.basket.added}")
    private String basketAddedHeader;
    @Value("${ball.rabbitmq.header.basket.removed}")
    private String basketRemovedHeader;
    @Value("${ball.rabbitmq.header.product.details.viewed}")
    private String productDetailsViewedHeader;

    private final RabbitTemplate template;
    private final ObjectMapper mapper;

    @Override
    public void orderCreated(OrderCreatedEvent orderCreatedEvent) {
        this.broadcast(orderCreatedEvent, this.orderCreatedHeader);
    }

    @Override
    public void basketItemAdded(BasketItemAddedEvent basketItemAddedEvent) {
        this.broadcast(basketItemAddedEvent, this.basketAddedHeader);
    }

    @Override
    public void basketItemRemoved(BasketItemRemovedEvent basketItemRemovedEvent) {
        this.broadcast(basketItemRemovedEvent, this.basketRemovedHeader);
    }

    @Override
    public void productDetailsViewed(ProductDetailsViewedEvent productDetailsViewedEvent) {
        this.broadcast(productDetailsViewedEvent, this.productDetailsViewedHeader);
    }

    private void broadcast(Event event, String messageType) {
        try {
            String eventJson = this.mapper.writeValueAsString(event);
            this.template.convertAndSend(this.exchangeKey, this.queueKey, eventJson, m -> {
                m.getMessageProperties().setHeader("MessageType", messageType);
                return m;
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
