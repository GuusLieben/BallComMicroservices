package com.ball.shipping.rest;

import com.ball.shipping.model.mssql.Shipment;
import com.ball.shipping.model.mssql.Shipper;
import com.ball.shipping.repository.ShipmentRepository;
import com.ball.shipping.repository.ShipperRepository;
import com.ball.shipping.amqp.RabbitMQSender;

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
    private final RabbitMQSender sender;

    public ShipmentController(ShipmentRepository shipmentRepository, ShipperRepository shipperRepository, RabbitMQSender sender) {
        this.shipmentRepository = shipmentRepository;
        this.shipperRepository = shipperRepository;
        this.sender = sender;
    }

    @RequestMapping(value = "/shipments", method = RequestMethod.POST)
    public Shipment createShipment(@RequestBody Shipment shipment) {
        if (shipment.getShipper() != null) {
            if (shipment.getShipper().getShipperId() != null) {
                Shipper shipper = this.shipperRepository.findFirstByShipperIdEquals(shipment.getShipper().getShipperId());
                shipment.setShipper(shipper);
            } else {
                shipment.setShipper(this.shipperRepository.save(shipment.getShipper()));
            }
        }
        Shipment savedShipment = this.shipmentRepository.save(shipment);
        this.sender.shipmentRegistered(savedShipment);
        savedShipment.getShipper().setShipment(null);
        return savedShipment;
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
