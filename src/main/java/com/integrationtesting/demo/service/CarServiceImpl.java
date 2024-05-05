package com.integrationtesting.demo.service;

import com.integrationtesting.demo.model.Car;
import com.integrationtesting.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<Car> getAllAvailableCars() {
        return carRepository.findAllAvailableCars();
    }

    @Override
    public Car updateCarAvailability(Long carId, boolean available) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        car.setAvailable(available);
        return carRepository.save(car);
    }

    @Override
    public Car markCarAsRented(Long carId) {
        return updateCarAvailability(carId, false);
    }

    @Override
    public double calculateAverageYearOfAvailableCars() {
        List<Car> availableCars = carRepository.findAllAvailableCars();

        if (availableCars.isEmpty()) {
            return 0.0;
        }

        int totalYears = availableCars.stream().mapToInt(Car::getYear).sum();
        return (double) totalYears / availableCars.size();
    }
}
