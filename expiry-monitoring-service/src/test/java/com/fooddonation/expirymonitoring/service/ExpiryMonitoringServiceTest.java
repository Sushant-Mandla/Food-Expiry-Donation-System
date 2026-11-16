
package com.fooddonation.expirymonitoring.service;

import com.fooddonation.expirymonitoring.client.InventoryClient;
import com.fooddonation.expirymonitoring.dto.FoodItemDto;
import com.fooddonation.expirymonitoring.dto.ExpiryFoodItemDto;
import com.fooddonation.expirymonitoring.enums.ExpiryStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpiryMonitoringServiceTest {

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private ExpiryMonitoringService expiryMonitoringService;

    private FoodItemDto createFoodItem(String name, LocalDate expiryDate) {
        FoodItemDto food = new FoodItemDto();
        food.setId(1L);
        food.setFoodName(name);
        food.setCategory("Vegetables");
        food.setQuantity(5.0);
        food.setUnit("kg");
        food.setExpiryDate(expiryDate);
        food.setLocation("Bengaluru");
        food.setProviderName("Test Provider");
        food.setProviderType("RESTAURANT");
        return food;
    }

    @Test
    void shouldIdentifyExpiredFood() {
        FoodItemDto food = createFoodItem(
                "Old Tomatoes", LocalDate.now().minusDays(2));

        when(inventoryClient.getAllFoodItems()).thenReturn(List.of(food));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getAllFoodItemsWithExpiryStatus();

        assertEquals(1, result.size());
        assertEquals(ExpiryStatus.EXPIRED, result.get(0).getExpiryStatus());
        assertEquals(-2, result.get(0).getDaysRemaining());
    }

    @Test
    void shouldIdentifyFoodExpiringToday() {
        FoodItemDto food = createFoodItem(
                "Fresh Milk", LocalDate.now());

        when(inventoryClient.getAllFoodItems()).thenReturn(List.of(food));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getAllFoodItemsWithExpiryStatus();

        assertEquals(ExpiryStatus.EXPIRING_SOON,
                result.get(0).getExpiryStatus());
        assertEquals(0, result.get(0).getDaysRemaining());
    }

    @Test
    void shouldIdentifyFoodExpiringWithinThreeDays() {
        FoodItemDto food = createFoodItem(
                "Bread", LocalDate.now().plusDays(3));

        when(inventoryClient.getAllFoodItems()).thenReturn(List.of(food));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getAllFoodItemsWithExpiryStatus();

        assertEquals(ExpiryStatus.EXPIRING_SOON,
                result.get(0).getExpiryStatus());
    }

    @Test
    void shouldIdentifySafeFood() {
        FoodItemDto food = createFoodItem(
                "Fresh Apples", LocalDate.now().plusDays(5));

        when(inventoryClient.getAllFoodItems()).thenReturn(List.of(food));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getAllFoodItemsWithExpiryStatus();

        assertEquals(ExpiryStatus.SAFE, result.get(0).getExpiryStatus());
        assertEquals(5, result.get(0).getDaysRemaining());
    }

    @Test
    void shouldReturnOnlyExpiredFoodItems() {
        FoodItemDto expired = createFoodItem(
                "Old Tomatoes", LocalDate.now().minusDays(1));

        FoodItemDto safe = createFoodItem(
                "Fresh Apples", LocalDate.now().plusDays(5));

        when(inventoryClient.getAllFoodItems())
                .thenReturn(List.of(expired, safe));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getExpiredFoodItems();

        assertEquals(1, result.size());
        assertEquals("Old Tomatoes", result.get(0).getFoodName());
    }

    @Test
    void shouldReturnOnlyFoodExpiringSoon() {
        FoodItemDto soon = createFoodItem(
                "Milk", LocalDate.now().plusDays(2));

        FoodItemDto safe = createFoodItem(
                "Apples", LocalDate.now().plusDays(7));

        when(inventoryClient.getAllFoodItems())
                .thenReturn(List.of(soon, safe));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getExpiringSoonFoodItems();

        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getFoodName());
    }

    @Test
    void shouldReturnOnlySafeFoodItems() {
        FoodItemDto safe = createFoodItem(
                "Apples", LocalDate.now().plusDays(7));

        FoodItemDto expired = createFoodItem(
                "Old Bread", LocalDate.now().minusDays(1));

        when(inventoryClient.getAllFoodItems())
                .thenReturn(List.of(safe, expired));

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getSafeFoodItems();

        assertEquals(1, result.size());
        assertEquals("Apples", result.get(0).getFoodName());
    }

    @Test
    void shouldReturnEmptyListWhenInventoryIsEmpty() {
        when(inventoryClient.getAllFoodItems()).thenReturn(List.of());

        List<ExpiryFoodItemDto> result =
                expiryMonitoringService.getAllFoodItemsWithExpiryStatus();

        assertTrue(result.isEmpty());
    }
}
