package com.fmi.project_01_car_rent.controllers;

import com.fmi.project_01_car_rent.entities.Car;
import com.fmi.project_01_car_rent.http.AppResponse;
import com.fmi.project_01_car_rent.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/cars")
    public ResponseEntity<?> createNewCar(@RequestBody Car car) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            carService.validateCar(car);
            if (this.carService.createCar(car)) {
                return AppResponse.success()
                        .withMessage("Car created successfully")
                        .build();
            } else {
                return AppResponse.error()
                        .withMessage("Problem during car creation")
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return AppResponse.error()
                    .withMessage(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars() {
        List<Car> cars = carService.getAllCars();
        if (cars.isEmpty()) {
            return AppResponse.error()
                    .withMessage("No cars found in the system.")
                    .build();
        }
        return AppResponse.success()
                .withData(cars)
                .build();
    }


    @GetMapping("/cars/location/{location}")
    public ResponseEntity<?> getCarByLocation(@PathVariable String location) {
        List<Car> cars = carService.getCarByLocation(location);
        if (cars.isEmpty()) {
            return AppResponse.error()
                    .withMessage("No cars found for location: " + location)
                    .build();
        }
        return AppResponse.success()
                .withData(cars)
                .build();
    }


    @GetMapping("/cars/{id}")
    public ResponseEntity<?> getCarById(@PathVariable int id) {
        try {
            Car car = carService.getCarById(id);
            return AppResponse.success()
                    .withData(car)
                    .build();
        } catch (RuntimeException ex) {
            return AppResponse.error()
                    .withMessage(ex.getMessage())
                    .build();
        }
    }


    @PutMapping("/cars/{id}")
    public ResponseEntity<?> updateCar(@PathVariable int id, @RequestBody Car car) {
        car.setId(id);
        try {
            boolean updateSuccess = carService.updateCar(car);

            if (updateSuccess) {
                return AppResponse.success()
                        .withMessage("Car updated successfully")
                        .build();
            } else {
                return AppResponse.error()
                        .withMessage("Car update failed")
                        .build();
            }
        } catch (RuntimeException ex) {
            return AppResponse.error()
                    .withMessage(ex.getMessage())
                    .build();
        }
    }


    @DeleteMapping("/cars/{id}")
    public ResponseEntity<?> removeCar(@PathVariable int id) {
        boolean isDeleteSuccessful = this.carService.deleteCar(id);

        if (!isDeleteSuccessful) {
            return AppResponse.error().withMessage("Car not Found").build();
        }

        return AppResponse.success().withMessage("Car removal successful").build();
    }

}
