package nl.avans.infrastructure.broker.events.send;

import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;

public class ProductDetailsViewedSendEvent extends ListenEvent {

    public ProductDetailsViewedSendEvent(Product product) {
        super("ProductDetailsViewed", product);
    }
}