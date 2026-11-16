
package com.fooddonation.foodinventory.repository;

import com.fooddonation.foodinventory.entity.FoodItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // Find food items by category
    List<FoodItem> findByCategoryIgnoreCase(String category);

    // Search food items by name
    List<FoodItem> findByFoodNameContainingIgnoreCase(String foodName);

    // Find food items expiring before or on a particular date
    List<FoodItem> findByExpiryDateLessThanEqual(LocalDate date);

    // Find food items expiring within a date range
    List<FoodItem> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

}
