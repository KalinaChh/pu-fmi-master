package com.fmi.project_01_car_rent.controllers;


import com.fmi.project_01_car_rent.entities.Car;
import com.fmi.project_01_car_rent.entities.Client;
import com.fmi.project_01_car_rent.entities.Offer;
import com.fmi.project_01_car_rent.http.AppResponse;
import com.fmi.project_01_car_rent.repositories.CarRepository;
import com.fmi.project_01_car_rent.repositories.ClientRepository;
import com.fmi.project_01_car_rent.repositories.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class OfferController {

    private final OfferRepository offerRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public OfferController(OfferRepository offerRepository, CarRepository carRepository, ClientRepository clientRepository) {
        this.offerRepository = offerRepository;
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/offers")
    public ResponseEntity<Object> createOffer(@RequestBody Offer offer) {
        // Validate Client and Car
        Client client = clientRepository.findById((long) offer.getClientId());
        if (client == null) {
            return AppResponse.error()
                    .withMessage("Client not found.")
                    .withCode(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Car car = carRepository.getCarById(offer.getCarId());
        if (car == null) {
            return AppResponse.error()
                    .withMessage("Car not found.")
                    .withCode(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Double basePrice = car.getPricePerDay()*(offer.getRentalDays());
        Double weekendSurcharge = (double) 0;
        if (offer.getRentalDays() > 2) { // Assume weekend is 2 days
            weekendSurcharge = Double.valueOf(basePrice*0.01);
        }

        Double accidentPenalty = client.hasAccidents() ? 200.0 : 0.0;
        Double totalPrice = basePrice+weekendSurcharge+ accidentPenalty;


        offer.setClientId(offer.getClientId());
        offer.setCarId(offer.getCarId());
        offer.setRentalDays(offer.getRentalDays());
        offer.setTotalPrice(totalPrice);

        offerRepository.createOffer(offer);

        return AppResponse.success()
                .withMessage("Offer created successfully.")
                .withCode(HttpStatus.CREATED)
                .build();
    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<Object> getOffersForClient(@PathVariable int clientId) {
        List<Offer> offers = offerRepository.getOffersByClientId(clientId);
        if (offers.isEmpty()) {
            return AppResponse.error()
                    .withMessage("No offers found for the client.")
                    .withCode(HttpStatus.NOT_FOUND)
                    .build();
        }
        return AppResponse.success()
                .withMessage("Offers retrieved successfully.")
                .withData(offers)
                .withCode(HttpStatus.OK)
                .build();
    }


    @GetMapping("/offer/{id}")
    public ResponseEntity<Object> getOfferById(@PathVariable int id) {
        Offer offer = offerRepository.getOfferById(id);
        if (offer == null) {
            return AppResponse.error()
                    .withMessage("Offer not found.")
                    .withCode(HttpStatus.NOT_FOUND)
                    .build();
        }
        return AppResponse.success()
                .withMessage("Offer retrieved successfully.")
                .withData(offer)
                .withCode(HttpStatus.OK)
                .build();
    }


    @DeleteMapping("/offer/{id}")
    public ResponseEntity<Object> deleteOffer(@PathVariable int id) {
        boolean isDeleted = offerRepository.softDeleteOffer(id);
        if (!isDeleted) {
            return AppResponse.error()
                    .withMessage("Offer not found or already deleted.")
                    .withCode(HttpStatus.NOT_FOUND)
                    .build();
        }
        return AppResponse.success()
                .withMessage("Offer deleted successfully.")
                .withCode(HttpStatus.OK)
                .build();
    }

    @PutMapping("/offer/{id}/accept")
    public ResponseEntity<Object> acceptOffer(@PathVariable int id) {
        boolean isAccepted = offerRepository.acceptOffer(id);
        if (!isAccepted) {
            return AppResponse.error()
                    .withMessage("Offer not found or already accepted/deleted.")
                    .withCode(HttpStatus.NOT_FOUND)
                    .build();
        }
        return AppResponse.success()
                .withMessage("Offer accepted successfully.")
                .withCode(HttpStatus.OK)
                .build();
    }



}
