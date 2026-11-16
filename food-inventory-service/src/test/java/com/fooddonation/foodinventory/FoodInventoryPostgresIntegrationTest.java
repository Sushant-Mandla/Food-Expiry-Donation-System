
package com.fooddonation.foodinventory;

import com.fooddonation.foodinventory.entity.FoodItem;
import com.fooddonation.foodinventory.repository.FoodItemRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FoodInventoryPostgresIntegrationTest {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Test
    void shouldSaveAndRetrieveFoodItemFromPostgreSQL() {

        FoodItem food = new FoodItem();

        food.setFoodName("Integration Test Apples");
        food.setCategory("Fruits");
        food.setQuantity(10.0);
        food.setUnit("Kg");
        food.setExpiryDate(LocalDate.now().plusDays(5));
        food.setLocation("Bengaluru");
        food.setProviderName("Test Supermarket");
        food.setProviderType("SUPERMARKET");

        FoodItem savedFood = foodItemRepository.save(food);

        assertNotNull(savedFood.getId());

        FoodItem retrievedFood = foodItemRepository
                .findById(savedFood.getId())
                .orElse(null);

        assertNotNull(retrievedFood);
        assertEquals("Integration Test Apples",
                retrievedFood.getFoodName());

        assertNotNull(retrievedFood.getCreatedAt());

        foodItemRepository.deleteById(savedFood.getId());
    }
}
