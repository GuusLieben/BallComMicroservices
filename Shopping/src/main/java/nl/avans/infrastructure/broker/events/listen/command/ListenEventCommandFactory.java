package nl.avans.infrastructure.broker.events.listen.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.write_db.RepositoryWriteDBFactory;

public class ListenEventCommandFactory {
    public static void execute(String type, String listenEvent) {
        ListenEvent event = null;
        ObjectMapper mapper = new ObjectMapper();
        RepositoryAbstractFactory repositoryAbstractFactory = new RepositoryWriteDBFactory();

        try {
            switch (type) {
                case "CustomerAdded":
                    event = mapper.readValue(listenEvent, CustomerAddedListenEvent.class);
                    break;
                case "CustomerUpdated":
                    break;
                case "StockAdded":
                    event = mapper.readValue(listenEvent, StockAddedListenEvent.class);
                    break;
                case "StockRemoved":
                    event = mapper.readValue(listenEvent, StockRemovedListenEvent.class);
                    break;
                case "ProductCreated":
                    event = mapper.readValue(listenEvent, ProductCreatedListenEvent.class);
                    break;
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        if (event != null) {
            event.execute(repositoryAbstractFactory);
        }
    }
}
