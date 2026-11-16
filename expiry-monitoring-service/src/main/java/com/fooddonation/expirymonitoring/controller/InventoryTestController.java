
package com.fooddonation.expirymonitoring.controller;

import com.fooddonation.expirymonitoring.client.InventoryClient;
import com.fooddonation.expirymonitoring.dto.FoodItemDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/expiry")
@Tag(
    name = "Inventory Integration",
    description = "Endpoints for accessing Food Inventory Service data"
)
public class InventoryTestController {

    private final InventoryClient inventoryClient;

    public InventoryTestController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @Operation(
    summary = "Get inventory items from Food Inventory Service",
    description = "Fetches all food items by communicating with the Food Inventory Service through Eureka service discovery."
)
@GetMapping("/inventory")
public List<FoodItemDto> getInventoryItems() {
    return inventoryClient.getAllFoodItems();
}
}
