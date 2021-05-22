package nl.avans.infrastructure.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.BasketItem;
import nl.avans.domain.models.Customer;
import nl.avans.domain.models.Order;
import nl.avans.infrastructure.broker.events.send.BasketItemAddedSendEvent;
import nl.avans.infrastructure.broker.events.send.OrderCreatedSendEvent;
import nl.avans.infrastructure.broker.events.send.SendEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

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
    public void orderCreated(Order order) {
        this.broadcast(new OrderCreatedSendEvent(order), this.orderCreatedHeader);
    }

    @Override
    public void basketItemAdded(BasketItem basketItem, UUID customerId) {
        this.broadcast(new BasketItemAddedSendEvent(basketItem, customerId), this.basketAddedHeader);
    }

    @Override
    public void basketItemRemoved(BasketItem basketItem, UUID customerI) {

    }

    private void broadcast(SendEvent event, String messageType) {
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
