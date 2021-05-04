package org.dockbox.climate.rest;

import org.dockbox.climate.model.mssql.Shipment;
import org.dockbox.climate.repository.ShipmentRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;

    public ShipmentController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @RequestMapping("/api/{id}")
    public Iterable<Shipment> getUsers(@PathVariable("id") long id) {
        return this.shipmentRepository.findAllByShipmentIdEquals(id);
    }

    @RequestMapping("/api")
    public Iterable<Shipment> getAllUsers() {
        return this.shipmentRepository.findAll();
    }

}
