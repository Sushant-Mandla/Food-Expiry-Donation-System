
package com.fooddonation.foodinventory.repository;

import com.fooddonation.foodinventory.entity.FoodItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FoodItemRepositoryTest {

    @Autowired
    private FoodItemRepository foodItemRepository;

    private FoodItem food1;
    private FoodItem food2;

    @BeforeEach
    void setUp() {
        foodItemRepository.deleteAll();

        food1 = new FoodItem();
        food1.setFoodName("Fresh Tomatoes");
        food1.setCategory("Vegetables");
        food1.setQuantity(50.0);
        food1.setUnit("kg");
        food1.setExpiryDate(LocalDate.of(2026, 11, 20));
        food1.setLocation("Bangalore");
        food1.setProviderName("Fresh Mart");
        food1.setProviderType("SUPERMARKET");

        food2 = new FoodItem();
        food2.setFoodName("Fresh Apples");
        food2.setCategory("Fruits");
        food2.setQuantity(30.0);
        food2.setUnit("kg");
        food2.setExpiryDate(LocalDate.of(2026, 11, 25));
        food2.setLocation("Bangalore");
        food2.setProviderName("Green Store");
        food2.setProviderType("SUPERMARKET");

        foodItemRepository.saveAll(List.of(food1, food2));
    }

    @Test
    void testFindByCategoryIgnoreCase() {
        List<FoodItem> result =
                foodItemRepository.findByCategoryIgnoreCase("vegetables");

        assertEquals(1, result.size());
        assertEquals("Fresh Tomatoes", result.get(0).getFoodName());
    }

    @Test
    void testFindByFoodNameContainingIgnoreCase() {
        List<FoodItem> result =
                foodItemRepository.findByFoodNameContainingIgnoreCase("fresh");

        assertEquals(2, result.size());
    }

    @Test
    void testFindByExpiryDateLessThanEqual() {
        List<FoodItem> result =
                foodItemRepository.findByExpiryDateLessThanEqual(
                        LocalDate.of(2026, 11, 20));

        assertEquals(1, result.size());
        assertEquals("Fresh Tomatoes", result.get(0).getFoodName());
    }

    @Test
    void testFindByExpiryDateBetween() {
        List<FoodItem> result =
                foodItemRepository.findByExpiryDateBetween(
                        LocalDate.of(2026, 11, 19),
                        LocalDate.of(2026, 11, 26));

        assertEquals(2, result.size());
    }

    @Test
    void testSaveFoodItem() {
        FoodItem food = new FoodItem();
        food.setFoodName("Fresh Carrots");
        food.setCategory("Vegetables");
        food.setQuantity(20.0);
        food.setUnit("kg");
        food.setExpiryDate(LocalDate.of(2026, 11, 30));
        food.setLocation("Bangalore");
        food.setProviderName("Local Store");
        food.setProviderType("SUPERMARKET");

        FoodItem saved = foodItemRepository.save(food);

        assertNotNull(saved.getId());
        assertEquals("Fresh Carrots", saved.getFoodName());
    }

    @Test
    void testFindById() {
        FoodItem saved = foodItemRepository.save(food1);

        FoodItem result = foodItemRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals("Fresh Tomatoes", result.getFoodName());
    }

    @Test
    void testDeleteFoodItem() {
        FoodItem saved = foodItemRepository.save(food1);

        foodItemRepository.deleteById(saved.getId());

        assertFalse(foodItemRepository.existsById(saved.getId()));
    }
}
