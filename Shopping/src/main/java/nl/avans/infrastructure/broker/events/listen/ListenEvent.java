package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import nl.avans.domain.models.Product;

import java.io.Serializable;

@Getter
public abstract class ListenEvent implements Serializable {

    @JsonProperty
    private String name;

    @JsonProperty
    private Product product;

    protected ListenEvent(String name, Product product) {
        this.name = name;
        this.product = product;
    }
}

