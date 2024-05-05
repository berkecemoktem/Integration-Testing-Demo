package com.integrationtesting.demo.service;

import com.integrationtesting.demo.model.Car;
import com.integrationtesting.demo.model.Rental;
import java.util.List;
import java.util.Optional;

public interface RentalService {
    List<Rental> getAllRentals();
    Optional<Rental> getRentalById(Long id);
    void deleteRental(Long id);
    List<Rental> getRentalsByUser(Long userId);
    Rental saveRental(Rental rental);
    void returnRental(Long rentalId);
    double calculateRentalCost(Car car, long rentalDays);
}
