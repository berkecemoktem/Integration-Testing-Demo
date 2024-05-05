package com.integrationtesting.demo.service;

import com.integrationtesting.demo.model.Car;
import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> getAllCars();
    Optional<Car> getCarById(Long id);
    Car saveCar(Car car);
    void deleteCar(Long id);
    List<Car> getAllAvailableCars();
    Car updateCarAvailability(Long carId, boolean available);
    Car markCarAsRented(Long carId);
    double calculateAverageYearOfAvailableCars();
}
