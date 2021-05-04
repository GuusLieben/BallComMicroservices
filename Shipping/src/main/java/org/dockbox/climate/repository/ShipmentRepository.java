package org.dockbox.climate.repository;

import org.dockbox.climate.model.mssql.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, String> {

    Shipment findFirstByShipmentIdEquals(long shipmentId);

}
