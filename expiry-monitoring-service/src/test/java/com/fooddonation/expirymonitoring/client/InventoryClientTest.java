
package com.fooddonation.expirymonitoring.client;

import com.fooddonation.expirymonitoring.dto.FoodItemDto;
import com.fooddonation.expirymonitoring.exception.InventoryServiceUnavailableException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class InventoryClientTest {

    private InventoryClient inventoryClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();

        mockServer = MockRestServiceServer.bindTo(builder).build();

        inventoryClient = new InventoryClient(builder);
    }

    @Test
    void shouldFetchAllFoodItemsSuccessfully() {
        mockServer.expect(requestTo("http://FOOD-INVENTORY-SERVICE/api/food"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess("""
                    [
                      {
                        "id": 1,
                        "foodName": "Fresh Tomatoes",
                        "category": "Vegetables",
                        "quantity": 5.0,
                        "unit": "kg",
                        "expiryDate": "2026-11-22",
                        "location": "Bengaluru",
                        "providerName": "Test Provider",
                        "providerType": "RESTAURANT"
                      }
                    ]
                    """, org.springframework.http.MediaType.APPLICATION_JSON));

        List<FoodItemDto> result = inventoryClient.getAllFoodItems();

        assertEquals(1, result.size());
        assertEquals("Fresh Tomatoes", result.get(0).getFoodName());

        mockServer.verify();
    }

    @Test
    void shouldReturnEmptyListWhenInventoryIsEmpty() {
        mockServer.expect(requestTo("http://FOOD-INVENTORY-SERVICE/api/food"))
                .andRespond(withSuccess("[]",
                        org.springframework.http.MediaType.APPLICATION_JSON));

        List<FoodItemDto> result = inventoryClient.getAllFoodItems();

        assertTrue(result.isEmpty());
        mockServer.verify();
    }

    @Test
    void shouldThrowExceptionWhenInventoryServiceReturnsServerError() {
        mockServer.expect(requestTo("http://FOOD-INVENTORY-SERVICE/api/food"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(
                InventoryServiceUnavailableException.class,
                () -> inventoryClient.getAllFoodItems()
        );

        mockServer.verify();
    }
}
