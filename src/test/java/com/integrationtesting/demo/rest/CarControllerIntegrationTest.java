package com.integrationtesting.demo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationtesting.demo.model.Car;
import com.integrationtesting.demo.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @Test
    public void testGetAllCars() throws Exception {
        List<Car> testCars = Arrays.asList(
                new Car("Toyota", "Camry", 2020, true, 50.0),
                new Car("Honda", "Civic", 2019, true, 45.0)
        );

        Mockito.when(carService.getAllCars()).thenReturn(testCars);

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(testCars.size()));
    }

    @Test
    public void testGetCarById() throws Exception {
        Long carId = 1L;
        Car car = new Car("Toyota", "Camry", 2020, true, 50.0);
        car.setId(carId); // Set the ID for the Car object
        Mockito.when(carService.getCarById(carId)).thenReturn(Optional.of(car));

        mockMvc.perform(get("/api/cars/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(carId)); // Validate the ID in the JSON response
    }


    @Test
    public void testSaveCar() throws Exception {
        Car newCar = new Car("Ford", "Mustang", 2022, true, 60.0);
        Car savedCar = new Car("Ford", "Mustang", 2022, true, 60.0);
        savedCar.setId(1L); // Set a simulated generated ID
        Mockito.when(carService.saveCar(any(Car.class))).thenReturn(savedCar);

        MvcResult result = mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCar)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Car returnedCar = objectMapper.readValue(responseBody, Car.class);

        // Verify the returned car matches the saved car
        assertCarEquals(savedCar, returnedCar);
    }

    @Test
    public void testDeleteCar() throws Exception {
        Long carId = 1L;
        mockMvc.perform(delete("/api/cars/{id}", carId))
                .andExpect(status().isNoContent());

        Mockito.verify(carService, Mockito.times(1)).deleteCar(carId);
    }

    private void assertCarEquals(Car expected, Car actual) {
        // Implement custom assertions for Car properties comparison
        // For simplicity, assuming all properties should match
        assert expected.getId().equals(actual.getId());
        assert expected.getBrand().equals(actual.getBrand());
        assert expected.getModel().equals(actual.getModel());
        assert expected.getYear() == actual.getYear();
        assert expected.isAvailable() == actual.isAvailable();
        assert expected.getDailyRate() == actual.getDailyRate();
    }
}
