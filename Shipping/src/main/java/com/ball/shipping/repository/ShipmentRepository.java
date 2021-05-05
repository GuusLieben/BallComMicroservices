package com.ball.shipping.repository;

import com.ball.shipping.model.mssql.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, String> {

    Shipment findFirstByShipmentIdEquals(long shipmentId);

}
