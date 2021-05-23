package nl.avans.domain.models.message;

import lombok.Data;

@Data
public class ReturnObject {
    private String error;
    private Object object;
}
