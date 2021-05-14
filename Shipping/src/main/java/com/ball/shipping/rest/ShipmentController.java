package com.ball.shipping.rest;

import com.ball.shipping.amqp.RabbitMQSender;
import com.ball.shipping.model.ErrorObject;
import com.ball.shipping.model.common.ShipmentState;
import com.ball.shipping.model.mssql.Shipment;
import com.ball.shipping.model.mssql.Shipper;
import com.ball.shipping.model.rest.ShipmentUpdate;
import com.ball.shipping.repository.ShipmentRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class ShipmentController {

    private final ShipmentRepository shipments;
    private final RabbitMQSender sender;

    public ShipmentController(ShipmentRepository shipments, RabbitMQSender sender) {
        this.shipments = shipments;
        this.sender = sender;
    }

    @RequestMapping(value = "/shipments/{id}", method = RequestMethod.PATCH)
    public Object processShipment(@PathVariable("id") long id, @RequestBody ShipmentUpdate update) {
        update.setShipmentId(id);
        Optional<Shipment> lookup = this.shipments.findById(id);
        if (lookup.isPresent()) {
            Shipment shipment = lookup.get();
            if (update.getState() == ShipmentState.REGISTERED) {
                return shipment;
            }
            else {
                shipment.setState(update.getState());
                Shipment savedShipment = this.shipments.save(shipment);
                savedShipment.getState().perform(this.sender, savedShipment);
                savedShipment.getShipper().setShipment(null);
                return savedShipment;
            }
        } else {
            return new ErrorObject(404, "Not found", "Could not find a shipment with id " + id);
        }
    }

    @RequestMapping("/shipments/{id}")
    public Object getShipment(@PathVariable("id") long id) {
        Optional<Shipment> lookup = this.shipments.findById(id);
        if (lookup.isPresent()) {
            Shipment shipment = lookup.get();
            shipment.getShipper().setShipment(null);
            this.sender.shipmentRegistered(shipment);
            shipment.setShipmentId(-1L); // Exclude from results
            return shipment;
        } else {
            return new ErrorObject(404, "Not found", "Could not find a shipment with id " + id);
        }
    }

    @RequestMapping("/shipments")
    public Object getAllShipments() {
        return StreamSupport.stream(this.shipments.findAll().spliterator(), false)
                .peek(shipment -> {
                    Shipper shipper = shipment.getShipper();
                    Shipper wrapper = new Shipper();
                    wrapper.setShipperId(shipper.getShipperId());
                    shipment.setShipper(wrapper);
                })
                .collect(Collectors.toList());
    }



}
