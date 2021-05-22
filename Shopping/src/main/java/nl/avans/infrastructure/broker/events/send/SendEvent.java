package nl.avans.infrastructure.broker.events.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

public interface SendEvent extends Serializable {
}
