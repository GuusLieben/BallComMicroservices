package nl.avans.infrastructure.broker.events.listen;

public interface ProductListener {
    void execute(String type, String payload);
}
