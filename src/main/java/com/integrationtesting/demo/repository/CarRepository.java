package com.integrationtesting.demo.repository;

import com.integrationtesting.demo.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Custom query to find all available cars
    @Query("SELECT c FROM Car c WHERE c.available = true")
    List<Car> findAllAvailableCars();
}