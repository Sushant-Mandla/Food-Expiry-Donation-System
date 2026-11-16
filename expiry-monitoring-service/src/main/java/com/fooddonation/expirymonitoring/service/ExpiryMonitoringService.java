
package com.fooddonation.expirymonitoring.service;

import com.fooddonation.expirymonitoring.client.InventoryClient;
import com.fooddonation.expirymonitoring.dto.ExpiryFoodItemDto;
import com.fooddonation.expirymonitoring.dto.FoodItemDto;
import com.fooddonation.expirymonitoring.enums.ExpiryStatus;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ExpiryMonitoringService {

    private static final long EXPIRY_WARNING_DAYS = 3;

    private final InventoryClient inventoryClient;

    public ExpiryMonitoringService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    // Retrieve all inventory items and calculate their expiry status
    public List<ExpiryFoodItemDto> getAllFoodItemsWithExpiryStatus() {

        List<FoodItemDto> foodItems = inventoryClient.getAllFoodItems();

        return foodItems.stream()
                .map(this::convertToExpiryFoodItemDto)
                .toList();
    }

    // Convert inventory DTO into expiry DTO
    private ExpiryFoodItemDto convertToExpiryFoodItemDto(FoodItemDto foodItem) {

        LocalDate today = LocalDate.now();

        long daysRemaining = ChronoUnit.DAYS.between(
                today,
                foodItem.getExpiryDate()
        );

        ExpiryStatus status;

        if (foodItem.getExpiryDate().isBefore(today)) {
            status = ExpiryStatus.EXPIRED;
        } else if (daysRemaining <= EXPIRY_WARNING_DAYS) {
            status = ExpiryStatus.EXPIRING_SOON;
        } else {
            status = ExpiryStatus.SAFE;
        }

        ExpiryFoodItemDto result = new ExpiryFoodItemDto();

        result.setId(foodItem.getId());
        result.setFoodName(foodItem.getFoodName());
        result.setCategory(foodItem.getCategory());
        result.setQuantity(foodItem.getQuantity());
        result.setUnit(foodItem.getUnit());
        result.setExpiryDate(foodItem.getExpiryDate());
        result.setLocation(foodItem.getLocation());
        result.setProviderName(foodItem.getProviderName());
        result.setProviderType(foodItem.getProviderType());

        result.setExpiryStatus(status);
        result.setDaysRemaining(daysRemaining);

        return result;
    }

    // Get only expired food items
    public List<ExpiryFoodItemDto> getExpiredFoodItems() {

        return getAllFoodItemsWithExpiryStatus()
                .stream()
                .filter(item -> item.getExpiryStatus() == ExpiryStatus.EXPIRED)
                .toList();
    }

    // Get food items expiring within the next 3 days
    public List<ExpiryFoodItemDto> getExpiringSoonFoodItems() {

        return getAllFoodItemsWithExpiryStatus()
                .stream()
                .filter(item -> item.getExpiryStatus() == ExpiryStatus.EXPIRING_SOON)
                .toList();
    }

    // Get food items that are still safe
    public List<ExpiryFoodItemDto> getSafeFoodItems() {

        return getAllFoodItemsWithExpiryStatus()
                .stream()
                .filter(item -> item.getExpiryStatus() == ExpiryStatus.SAFE)
                .toList();
    }

}
