
package com.fooddonation.expirymonitoring.controller;

import com.fooddonation.expirymonitoring.dto.ExpiryFoodItemDto;
import com.fooddonation.expirymonitoring.service.ExpiryMonitoringService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expiry")
@Tag(
    name = "Expiry Monitoring",
    description = "APIs for monitoring food expiry status"
)
public class ExpiryMonitoringController {

    private final ExpiryMonitoringService expiryMonitoringService;

    public ExpiryMonitoringController(
            ExpiryMonitoringService expiryMonitoringService) {
        this.expiryMonitoringService = expiryMonitoringService;
    }

    @Operation(
        summary = "Get all food expiry statuses",
        description = "Retrieves all food items from the Food Inventory Service and classifies them as EXPIRED, EXPIRING_SOON, or SAFE."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expiry statuses retrieved successfully"),
        @ApiResponse(responseCode = "503", description = "Food Inventory Service is unavailable")
    })
    @GetMapping("/status")
    public List<ExpiryFoodItemDto> getAllExpiryStatuses() {
        return expiryMonitoringService.getAllFoodItemsWithExpiryStatus();
    }

    @Operation(
        summary = "Get expired food items",
        description = "Retrieves food items whose expiry dates have already passed."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expired food items retrieved successfully"),
        @ApiResponse(responseCode = "503", description = "Food Inventory Service is unavailable")
    })
    @GetMapping("/expired")
    public List<ExpiryFoodItemDto> getExpiredFoodItems() {
        return expiryMonitoringService.getExpiredFoodItems();
    }

    @Operation(
        summary = "Get food items expiring soon",
        description = "Retrieves food items that expire today or within the next three days."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expiring food items retrieved successfully"),
        @ApiResponse(responseCode = "503", description = "Food Inventory Service is unavailable")
    })
    @GetMapping("/expiring-soon")
    public List<ExpiryFoodItemDto> getExpiringSoonFoodItems() {
        return expiryMonitoringService.getExpiringSoonFoodItems();
    }

    @Operation(
        summary = "Get safe food items",
        description = "Retrieves food items with more than three days remaining before expiry."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Safe food items retrieved successfully"),
        @ApiResponse(responseCode = "503", description = "Food Inventory Service is unavailable")
    })
    @GetMapping("/safe")
    public List<ExpiryFoodItemDto> getSafeFoodItems() {
        return expiryMonitoringService.getSafeFoodItems();
    }
}
