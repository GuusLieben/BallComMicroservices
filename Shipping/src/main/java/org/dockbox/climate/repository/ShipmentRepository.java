package org.dockbox.climate.repository;

import org.dockbox.climate.model.mssql.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, String> {

    Iterable<Shipment> findAllByShipmentIdEquals(long userId);

}
