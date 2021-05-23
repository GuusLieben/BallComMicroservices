package nl.avans.infrastructure.broker;

public interface ListenEventFactory {
    void execute(String type, String listenEvent);
}
