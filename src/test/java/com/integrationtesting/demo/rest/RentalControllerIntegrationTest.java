package com.integrationtesting.demo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationtesting.demo.model.Rental;
import com.integrationtesting.demo.service.RentalService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc
public class RentalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RentalService rentalService;

    @Test
    public void testGetAllRentals() throws Exception {
        // Prepare test data
        List<Rental> testRentals = Arrays.asList(
                new Rental(),
                new Rental()
        );

        // Mock the service method
        Mockito.when(rentalService.getAllRentals()).thenReturn(testRentals);

        // Perform GET request and verify the response
        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(testRentals.size()));
    }

    @Test
    public void testGetRentalById_ExistingId() throws Exception {
        // Prepare test data
        Long rentalId = 1L;
        Rental testRental = new Rental();
        testRental.setId(rentalId);

        // Mock the service method
        Mockito.when(rentalService.getRentalById(rentalId)).thenReturn(Optional.of(testRental));

        // Perform GET request and verify the response
        mockMvc.perform(get("/api/rentals/{id}", rentalId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(rentalId));
    }

    @Test
    public void testGetRentalById_NonExistingId() throws Exception {
        // Prepare test data
        Long rentalId = 999L;

        // Mock the service method
        Mockito.when(rentalService.getRentalById(rentalId)).thenReturn(Optional.empty());

        // Perform GET request and verify the response
        mockMvc.perform(get("/api/rentals/{id}", rentalId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveRental() throws Exception {
        // Prepare test data
        Rental newRental = new Rental();
        Rental savedRental = new Rental();
        savedRental.setId(1L);

        // Mock the service method
        Mockito.when(rentalService.saveRental(any(Rental.class))).thenReturn(savedRental);

        // Perform POST request and verify the response
        MvcResult result = mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRental)))
                .andExpect(status().isCreated())
                .andReturn();

        // Verify the response body contains the saved rental ID
        String responseBody = result.getResponse().getContentAsString();
        Rental returnedRental = objectMapper.readValue(responseBody, Rental.class);
        Long savedRentalId = savedRental.getId();

        // Assert the returned rental ID matches the saved rental ID
        assert savedRentalId != null;
        assert savedRentalId.equals(returnedRental.getId());
    }

    @Test
    public void testDeleteRental() throws Exception {
        // Prepare test data
        Long rentalId = 1L;

        // Perform DELETE request and verify the response
        mockMvc.perform(delete("/api/rentals/{id}", rentalId))
                .andExpect(status().isNoContent());

        // Verify that the service method was called with the correct ID
        Mockito.verify(rentalService, Mockito.times(1)).deleteRental(eq(rentalId));
    }

    @Test
    public void testReturnRental() throws Exception {
        // Prepare test data
        Long rentalId = 1L;

        // Perform PUT request and verify the response
        mockMvc.perform(put("/api/rentals/{id}/return", rentalId))
                .andExpect(status().isNoContent());

        // Verify that the service method was called with the correct ID
        Mockito.verify(rentalService, Mockito.times(1)).returnRental(eq(rentalId));
    }
}
