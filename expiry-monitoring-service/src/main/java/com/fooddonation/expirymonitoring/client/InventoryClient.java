
package com.fooddonation.expirymonitoring.client;

import com.fooddonation.expirymonitoring.dto.FoodItemDto;
import com.fooddonation.expirymonitoring.exception.InventoryServiceUnavailableException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(
        @Qualifier("loadBalancedRestClientBuilder")
        RestClient.Builder builder) {
    this.restClient = builder
            .baseUrl("http://FOOD-INVENTORY-SERVICE")
            .build();
}

    public List<FoodItemDto> getAllFoodItems() {
        try {
            List<FoodItemDto> foodItems = restClient.get()
                    .uri("/api/food")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<FoodItemDto>>() {});

            return foodItems != null ? foodItems : List.of();

        } catch (RestClientException ex) {
            throw new InventoryServiceUnavailableException(
                    "Unable to communicate with Food Inventory Service.",
                    ex
            );
        }
    }
}
