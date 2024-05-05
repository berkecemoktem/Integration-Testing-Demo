package com.integrationtesting.demo.rest;

import com.integrationtesting.demo.model.Car;
import com.integrationtesting.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        return car.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Car> saveCar(@RequestBody Car car) {
        Car savedCar = carService.saveCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAllAvailableCars() {
        List<Car> availableCars = carService.getAllAvailableCars();
        return ResponseEntity.ok(availableCars);
    }

    @PutMapping("/{carId}/availability")
    public ResponseEntity<Car> updateCarAvailability(@PathVariable Long carId,
                                                     @RequestParam boolean available) {
        Car updatedCar = carService.updateCarAvailability(carId, available);
        return ResponseEntity.ok(updatedCar);
    }

    @GetMapping("/average-year")
    public ResponseEntity<Double> getAverageYearOfAvailableCars() {
        double averageYear = carService.calculateAverageYearOfAvailableCars();
        return ResponseEntity.ok(averageYear);
    }
}
