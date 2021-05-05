package com.ball.shipping.rest;

import com.ball.shipping.amqp.RabbitMQSender;
import com.ball.shipping.model.common.ShipmentState;
import com.ball.shipping.model.mssql.Shipment;
import com.ball.shipping.model.mssql.Shipper;
import com.ball.shipping.model.rest.ShipmentUpdate;
import com.ball.shipping.repository.ShipmentRepository;
import com.ball.shipping.repository.ShipperRepository;

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

    @RequestMapping(value = "/shipments/{id}", method = RequestMethod.PATCH)
    public Shipment processShipment(@PathVariable("id") long id, @RequestBody ShipmentUpdate update) {
        update.setShipmentId(id);
        Shipment shipment = this.shipmentRepository.findFirstByShipmentIdEquals(id);
        if (update.getState() == ShipmentState.REGISTERED) {
            return shipment;
        } else {
            shipment.setState(update.getState());
            Shipment savedShipment = this.shipmentRepository.save(shipment);
            savedShipment.getState().perform(this.sender, savedShipment);
            savedShipment.getShipper().setShipment(null);
            return savedShipment;
        }
    }

    @RequestMapping("/shipments/{id}")
    public Shipment getShipment(@PathVariable("id") long id) {
        Shipment shipment = this.shipmentRepository.findFirstByShipmentIdEquals(id);
        shipment.getShipper().setShipment(null);
        this.sender.shipmentRegistered(shipment);
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
