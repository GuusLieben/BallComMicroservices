package org.dockbox.climate.rest;

import org.dockbox.climate.model.mssql.Shipment;
import org.dockbox.climate.model.mssql.Shipper;
import org.dockbox.climate.repository.ShipmentRepository;
import org.dockbox.climate.repository.ShipperRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;
    private final ShipperRepository shipperRepository;

    public ShipmentController(ShipmentRepository shipmentRepository, ShipperRepository shipperRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipperRepository = shipperRepository;
    }

    @RequestMapping(value = "/shipments", method = RequestMethod.POST)
    public Shipment createShipment(@RequestBody Shipment shipment) {
        if (shipment.getShipper() != null) {
            this.shipperRepository.save(shipment.getShipper());
        }
        return this.shipmentRepository.save(shipment);
    }

    @RequestMapping("/shipments/{id}")
    public Shipment getShipment(@PathVariable("id") long id) {
        Shipment shipment = this.shipmentRepository.findFirstByShipmentIdEquals(id);
        shipment.getShipper().setShipment(null);
        shipment.setShipmentId(-1L); // Exclude from results
        return shipment;
    }

    @RequestMapping("/shipments")
    public Object getAllShipments() {
        return StreamSupport.stream(this.shipmentRepository.findAll().spliterator(), false)
                .peek(shipment -> {
                    Shipper shipper = shipment.getShipper();
                    Shipper wrapper = new Shipper();
                    wrapper.setShipperId(shipper.getShipperId());
                    shipment.setShipper(wrapper);
                })
                .collect(Collectors.toList());
    }

}
