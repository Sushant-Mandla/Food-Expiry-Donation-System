
package com.fooddonation.foodinventory.service;

import com.fooddonation.foodinventory.entity.FoodItem;
import com.fooddonation.foodinventory.repository.FoodItemRepository;
import com.fooddonation.foodinventory.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @InjectMocks
    private FoodItemService foodItemService;

    // Helper method to create sample food item
    private FoodItem createFoodItem() {

        FoodItem food = new FoodItem();

        food.setFoodName("Fresh Tomatoes");
        food.setCategory("Vegetables");
        food.setQuantity(50.0);
        food.setUnit("kg");
        food.setExpiryDate(LocalDate.of(2026, 11, 20));
        food.setLocation("Bangalore");
        food.setProviderName("Fresh Mart Supermarket");

        return food;
    }

    // 1. Test adding a food item
    @Test
    void testAddFoodItem() {

        FoodItem food = createFoodItem();

        when(foodItemRepository.save(food)).thenReturn(food);

        FoodItem result = foodItemService.addFoodItem(food);

        assertNotNull(result);
        assertEquals("Fresh Tomatoes", result.getFoodName());

        verify(foodItemRepository, times(1)).save(food);
    }

    // 2. Test retrieving all food items
    @Test
    void testGetAllFoodItems() {

        List<FoodItem> foodList = List.of(createFoodItem());

        when(foodItemRepository.findAll()).thenReturn(foodList);

        List<FoodItem> result = foodItemService.getAllFoodItems();

        assertEquals(1, result.size());
        assertEquals("Fresh Tomatoes", result.get(0).getFoodName());

        verify(foodItemRepository, times(1)).findAll();
    }

    // 3. Test retrieving food item by ID
    @Test
    void testGetFoodItemById() {

        FoodItem food = createFoodItem();

        when(foodItemRepository.findById(1L))
                .thenReturn(Optional.of(food));

        FoodItem result = foodItemService.getFoodItemById(1L);

        assertNotNull(result);
        assertEquals("Fresh Tomatoes", result.getFoodName());

        verify(foodItemRepository).findById(1L);
    }

    // 4. Test retrieving food item with invalid ID
    @Test
    void testGetFoodItemByIdNotFound() {

        when(foodItemRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> foodItemService.getFoodItemById(999L)
        );

        verify(foodItemRepository).findById(999L);
    }

    // 5. Test updating a food item
    @Test
    void testUpdateFoodItem() {

        FoodItem existingFood = createFoodItem();

        FoodItem updatedFood = createFoodItem();
        updatedFood.setFoodName("Updated Tomatoes");
        updatedFood.setQuantity(40.0);

        when(foodItemRepository.findById(1L))
                .thenReturn(Optional.of(existingFood));

        when(foodItemRepository.save(existingFood))
                .thenReturn(existingFood);

        FoodItem result = foodItemService.updateFoodItem(1L, updatedFood);

        assertEquals("Updated Tomatoes", result.getFoodName());
        assertEquals(40.0, result.getQuantity());

        verify(foodItemRepository).findById(1L);
        verify(foodItemRepository).save(existingFood);
    }

    // 6. Test deleting a food item
    @Test
    void testDeleteFoodItem() {

        FoodItem food = createFoodItem();

        when(foodItemRepository.findById(1L))
                .thenReturn(Optional.of(food));

        foodItemService.deleteFoodItem(1L);

        verify(foodItemRepository).findById(1L);
        verify(foodItemRepository).delete(food);
    }

    // 7. Test searching food items by category
    @Test
    void testGetFoodItemsByCategory() {

        List<FoodItem> foodList = List.of(createFoodItem());

        when(foodItemRepository.findByCategoryIgnoreCase("Vegetables"))
                .thenReturn(foodList);

        List<FoodItem> result =
                foodItemService.getFoodItemsByCategory("Vegetables");

        assertEquals(1, result.size());
        assertEquals("Vegetables", result.get(0).getCategory());

        verify(foodItemRepository)
                .findByCategoryIgnoreCase("Vegetables");
    }

    // 8. Test searching food items by name
    @Test
    void testSearchFoodItemsByName() {

        List<FoodItem> foodList = List.of(createFoodItem());

        when(foodItemRepository.findByFoodNameContainingIgnoreCase("Tomatoes"))
                .thenReturn(foodList);

        List<FoodItem> result =
                foodItemService.searchFoodItemsByName("Tomatoes");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getFoodName().contains("Tomatoes"));

        verify(foodItemRepository)
                .findByFoodNameContainingIgnoreCase("Tomatoes");
    }

    // 9. Test finding food items expiring before a date
    @Test
    void testGetFoodItemsExpiringBefore() {

        LocalDate date = LocalDate.of(2026, 11, 25);

        List<FoodItem> foodList = List.of(createFoodItem());

        when(foodItemRepository.findByExpiryDateLessThanEqual(date))
                .thenReturn(foodList);

        List<FoodItem> result =
                foodItemService.getFoodItemsExpiringBefore(date);

        assertEquals(1, result.size());

        verify(foodItemRepository)
                .findByExpiryDateLessThanEqual(date);
    }

    // 10. Test finding food items between two dates
    @Test
    void testGetFoodItemsExpiringBetween() {

        LocalDate startDate = LocalDate.of(2026, 11, 15);
        LocalDate endDate = LocalDate.of(2026, 11, 25);

        List<FoodItem> foodList = List.of(createFoodItem());

        when(foodItemRepository.findByExpiryDateBetween(
                startDate, endDate))
                .thenReturn(foodList);

        List<FoodItem> result =
                foodItemService.getFoodItemsExpiringBetween(
                        startDate, endDate);

        assertEquals(1, result.size());

        verify(foodItemRepository)
                .findByExpiryDateBetween(startDate, endDate);
    }
}
