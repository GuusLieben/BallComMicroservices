package nl.avans.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private int amount;
    private String supplier;
    private String brand;
    private int detailsViewed;
}
