package nl.avans.infrastructure.broker;

import lombok.RequiredArgsConstructor;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQSender {
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

    private void broadcast(ListenEvent listenEvent, String messageType) {
        this.template.convertAndSend(this.exchangeKey, this.queueKey, listenEvent, m -> {
            m.getMessageProperties().setHeader("MessageType", messageType);
            return m;
        });
    }
}
