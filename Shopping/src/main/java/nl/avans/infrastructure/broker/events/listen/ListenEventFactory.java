package nl.avans.infrastructure.broker.events.listen;

public class ListenEventFactory {
    public static ListenEvent execute(String type, ListenEvent listenEvent) {
        ListenEvent event = null;
        switch (type) {
            case "CustomerAdded":
                event = new CustomerAddedListenEvent(listenEvent.getProduct());
                break;
            case "CustomerUpdated":
                System.out.println("Customer update");
                break;
            case "StockAdded":
                System.out.println("Add stock");
                break;
            case "StockRemoved":
                System.out.println("removed stock");
                break;
        }
        return event;
    }
}
