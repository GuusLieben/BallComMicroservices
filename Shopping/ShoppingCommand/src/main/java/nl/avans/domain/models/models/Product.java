package nl.avans.domain.models.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.DateFormat;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
    private UUID productId;
    private String name;
    private String description;
    private double price;
    private int amount;
    private String supplierName;
    private String brandName;
    private double weight;
    private int detailsViewed;
}
