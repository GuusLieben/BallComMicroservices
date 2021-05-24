package nl.avans.domain.services.aggregate;

import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.models.models.Product;

import java.util.ArrayList;

public interface AggregateProduct {
    Product aggregate(ArrayList<ProductEventModel> events);
}
