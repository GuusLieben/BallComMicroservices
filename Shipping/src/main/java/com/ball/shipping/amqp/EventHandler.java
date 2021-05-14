package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.order.OrderCreatedEvent;
import com.ball.shipping.model.amqp.shipment.ShipmentRegisteredEvent;
import com.ball.shipping.model.common.ShipmentState;
import com.ball.shipping.model.mssql.Shipment;
import com.ball.shipping.model.mssql.Shipper;
import com.ball.shipping.repository.ShipmentRepository;
import com.ball.shipping.repository.ShipperRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EventHandler {

    @Autowired
    private RabbitMQSender sender;

    @Autowired
    private ShipmentRepository shipments;

    @Autowired
    private ShipperRepository shippers;

    public void handle(ShipmentRegisteredEvent event) {
        System.out.println("Registered shipment!");
    }

    public void handle(OrderCreatedEvent event) {
        Shipment shipment = event.toShipment();
        shipment.setState(ShipmentState.REGISTERED);

        // Distance is at least 5km and at most 55km
        double distance = new Random(100).nextInt(55) + 5D;

        Shipper shipper = null;
        double currentBestPrice = -1.0D;
        for (Shipper candidate : this.shippers.findAll()) {
            if (shipment.getWeightInKg() > shipper.getMaxKg()) continue;
            if (shipper.getMaxKm() > shipper.getMaxKm()) continue;

            double totalPrice = 0.0D;
            totalPrice += candidate.getPricePerPackage();
            totalPrice += (candidate.getPricePerKg() * shipment.getWeightInKg());
            totalPrice += (candidate.getPricePerKm() * distance);

            if (currentBestPrice == -1.0D || totalPrice < currentBestPrice) {
                shipper = candidate;
                currentBestPrice = totalPrice;
            }
        }

        // Shipper can be null at this point, if it is this is considered a business fault and should be handled externally.
        shipment.setShipper(shipper);

        this.shipments.save(shipment);
        this.sender.shipmentRegistered(shipment);
    }

}
