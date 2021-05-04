package org.dockbox.climate.repository;

import org.dockbox.climate.model.mssql.Shipper;
import org.springframework.data.repository.CrudRepository;

public interface ShipperRepository extends CrudRepository<Shipper, String> {

    Shipper findFirstByShipperIdEquals(long shipperId);

}

