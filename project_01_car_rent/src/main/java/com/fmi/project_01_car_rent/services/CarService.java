package com.fmi.project_01_car_rent.services;

import com.fmi.project_01_car_rent.entities.Car;
import com.fmi.project_01_car_rent.repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    private static final Set<String> ALLOWED_LOCATIONS = Set.of("Plovdiv", "Sofia", "Varna", "Burgar"); //client is always right; kept burgaR

    public void validateCar(Car car) {
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Car brand cannot be null or empty");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Car model cannot be null or empty");
        }
        if (car.getLocation() == null || car.getLocation().trim().isEmpty() || !ALLOWED_LOCATIONS.contains(car.getLocation())) {
            throw new IllegalArgumentException("Car location must be one of: Plovdiv, Sofia, Varna, Burgar");
        }
        if (car.getPricePerDay() <= 0) {
            throw new IllegalArgumentException("Car price per day must be greater than 0");
        }
    }

    public boolean createCar(Car car) {
        return carRepository.createCar(car);
    }


    public List<Car> getAllCars() {
        return carRepository.getAllCars();
    }

    public Car getCarById(int id) {
        Car car = carRepository.getCarById(id);
        if (car == null) {
            throw new RuntimeException("Car not found with ID: " + id);
        }
        return car;    }

    public List<Car> getCarByLocation(String location) {
        return carRepository.getCarByLocation(location);
    }

    public List<Car> getCarByClientLocation(int clientId) {
        return carRepository.getCarsByClientLocation(clientId);
    }


    public boolean updateCar(Car car) {
        validateCar(car);

        boolean updateSuccess = carRepository.updateCar(car);

        if (!updateSuccess) {
            throw new RuntimeException("Car update failed or car not found with ID: " + car.getId());
        }

        return updateSuccess;
    }

    public boolean deleteCar(int id) {
        boolean success = carRepository.deleteCar(id);
        if (!success) {
            throw new RuntimeException("Car not found or already deleted with ID: " + id);
        }
        return success;
    }
}





