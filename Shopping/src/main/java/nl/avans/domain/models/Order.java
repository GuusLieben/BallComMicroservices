package nl.avans.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Order {
    private UUID customerId;
    private PaymentType paymentType;
    private String postCode;
    private String street;
    private String city;
    private int houseNumber;
    private String additional;
}
