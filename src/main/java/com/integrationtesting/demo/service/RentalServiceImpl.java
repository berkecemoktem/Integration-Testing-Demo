package com.integrationtesting.demo.service;

import com.integrationtesting.demo.model.Car;
import com.integrationtesting.demo.model.Rental;
import com.integrationtesting.demo.model.User;
import com.integrationtesting.demo.repository.CarRepository;
import com.integrationtesting.demo.repository.RentalRepository;
import com.integrationtesting.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public Optional<Rental> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

    @Override
    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }

    @Override
    public List<Rental> getRentalsByUser(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    @Override
    public Rental saveRental(Rental rental) {
        User user = userRepository.findById(rental.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(rental.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for rental");
        }

        rental.setUser(user);
        rental.setCar(car);

        long rentalDays = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
        double rentalCost = calculateRentalCost(car, rentalDays);

        rental.setTotalCost(rentalCost);

        Rental savedRental = rentalRepository.save(rental);
        carService.markCarAsRented(car.getId());

        return savedRental;
    }

    @Override
    public void returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.isReturned()) {
            throw new RuntimeException("Rental is already marked as returned");
        }

        rental.setReturned(true);

        Car rentedCar = rental.getCar();
        rentedCar.setAvailable(true);
        carRepository.save(rentedCar);

        rentalRepository.save(rental);
    }

    @Override
    public double calculateRentalCost(Car car, long rentalDays) {
        double dailyRentalRate = car.getDailyRate();
        return dailyRentalRate * rentalDays;
    }
}
