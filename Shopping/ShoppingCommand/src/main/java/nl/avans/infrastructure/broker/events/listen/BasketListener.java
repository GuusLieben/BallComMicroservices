package nl.avans.infrastructure.broker.events.listen;

public interface BasketListener {
    void execute(String type, String payload);
}
