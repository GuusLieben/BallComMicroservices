package com.ball.shipping.repository;

import com.ball.shipping.model.mssql.Shipper;
import org.springframework.data.repository.CrudRepository;

public interface ShipperRepository extends CrudRepository<Shipper, String> {

    Shipper findFirstByShipperIdEquals(long shipperId);

}

