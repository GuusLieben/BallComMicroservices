package nl.avans.domain.services.message;

import nl.avans.domain.models.events.basket.BasketItemAddedEvent;
import nl.avans.domain.models.events.basket.BasketItemRemovedEvent;
import nl.avans.domain.models.events.basket.OrderCreatedEvent;
import nl.avans.domain.models.events.product.ProductDetailsViewedEvent;

public interface BrokerMessageSender {
    void orderCreated(OrderCreatedEvent orderCreatedEvent);
    void basketItemAdded(BasketItemAddedEvent basketItemAddedEvent);
    void basketItemRemoved(BasketItemRemovedEvent basketItemRemovedEvent);
    void productDetailsViewed(ProductDetailsViewedEvent productDetailsViewedEvent);
}
