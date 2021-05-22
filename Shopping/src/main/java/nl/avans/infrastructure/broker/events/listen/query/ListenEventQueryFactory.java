package nl.avans.infrastructure.broker.events.listen.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.read_db.RepositoryReadDBFactory;

public class ListenEventQueryFactory {
    public static void execute(String type, String listenEvent) {
        ListenEvent event = null;
        ObjectMapper mapper = new ObjectMapper();
        RepositoryAbstractFactory repositoryAbstractFactory = new RepositoryReadDBFactory();

        try {
            switch (type) {
                case "CustomerAdded":
                    event = mapper.readValue(listenEvent, CustomerAddedQueryListenEvent.class);
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
