package nl.avans.domain.models.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Basket {
    private Customer customer;
    private ArrayList<BasketItem> products;
}
