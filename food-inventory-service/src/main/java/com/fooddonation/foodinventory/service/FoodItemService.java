
package com.fooddonation.foodinventory.service;

import com.fooddonation.foodinventory.entity.FoodItem;
import com.fooddonation.foodinventory.repository.FoodItemRepository;
import com.fooddonation.foodinventory.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    // Constructor injection
    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    // 1. Add a new food item
    public FoodItem addFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    // 2. Retrieve all food items
    @Transactional(readOnly = true)
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    // 3. Retrieve food item by ID
    @Transactional(readOnly = true)
    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "Food item not found with ID: " + id));
    }

    // 4. Update an existing food item
    public FoodItem updateFoodItem(Long id, FoodItem updatedFoodItem) {

        FoodItem existingFoodItem = getFoodItemById(id);

        existingFoodItem.setFoodName(updatedFoodItem.getFoodName());
        existingFoodItem.setCategory(updatedFoodItem.getCategory());
        existingFoodItem.setQuantity(updatedFoodItem.getQuantity());
        existingFoodItem.setUnit(updatedFoodItem.getUnit());
        existingFoodItem.setExpiryDate(updatedFoodItem.getExpiryDate());
        existingFoodItem.setLocation(updatedFoodItem.getLocation());
        existingFoodItem.setProviderName(updatedFoodItem.getProviderName());
        existingFoodItem.setProviderType(updatedFoodItem.getProviderType());

        return foodItemRepository.save(existingFoodItem);
    }

    // 5. Delete a food item
    public void deleteFoodItem(Long id) {

        FoodItem foodItem = getFoodItemById(id);

        foodItemRepository.delete(foodItem);
    }

    // 6. Search food items by category
    @Transactional(readOnly = true)
    public List<FoodItem> getFoodItemsByCategory(String category) {
        return foodItemRepository.findByCategoryIgnoreCase(category);
    }

    // 7. Search food items by name
    @Transactional(readOnly = true)
    public List<FoodItem> searchFoodItemsByName(String foodName) {
        return foodItemRepository.findByFoodNameContainingIgnoreCase(foodName);
    }

    // 8. Find expired or expiring food items
    @Transactional(readOnly = true)
    public List<FoodItem> getFoodItemsExpiringBefore(LocalDate date) {
        return foodItemRepository.findByExpiryDateLessThanEqual(date);
    }

    // 9. Find food items expiring within a date range
    @Transactional(readOnly = true)
    public List<FoodItem> getFoodItemsExpiringBetween(
            LocalDate startDate, LocalDate endDate) {

        return foodItemRepository.findByExpiryDateBetween(startDate, endDate);
    }
}
