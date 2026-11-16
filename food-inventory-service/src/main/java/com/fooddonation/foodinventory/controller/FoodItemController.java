
package com.fooddonation.foodinventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.fooddonation.foodinventory.entity.FoodItem;
import com.fooddonation.foodinventory.service.FoodItemService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodItemController {

    private final FoodItemService foodItemService;

    // Constructor injection
    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    // 1. Add a new food item
    @Operation(
    summary = "Add a new food item",
    description = "Registers a new food item in the inventory database."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Food item created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid food item details")
})
    @PostMapping
    public ResponseEntity<FoodItem> addFoodItem(
            @Valid @RequestBody FoodItem foodItem) {

        FoodItem savedFood = foodItemService.addFoodItem(foodItem);

        return new ResponseEntity<>(savedFood, HttpStatus.CREATED);
    }

    // 2. Get all food items
    @Operation(
    summary = "Get all food items",
    description = "Retrieves all food items currently registered in the inventory."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Food items retrieved successfully")
})
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {

        return ResponseEntity.ok(foodItemService.getAllFoodItems());
    }

    // 3. Get food item by ID
    @Operation(
    summary = "Get food item by ID",
    description = "Retrieves a specific food item using its unique ID."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Food item retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Food item not found")
})
    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(foodItemService.getFoodItemById(id));
    }

    // 4. Update a food item
    @Operation(
    summary = "Update a food item",
    description = "Updates the details of an existing food item using its ID."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Food item updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid food item details"),
    @ApiResponse(responseCode = "404", description = "Food item not found")
})
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(
            @PathVariable Long id,
            @Valid @RequestBody FoodItem foodItem) {

        FoodItem updatedFood = foodItemService.updateFoodItem(id, foodItem);

        return ResponseEntity.ok(updatedFood);
    }

    // 5. Delete a food item
    @Operation(
    summary = "Delete a food item",
    description = "Permanently deletes a food item from the inventory using its ID."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Food item deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Food item not found")
})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(
            @PathVariable Long id) {

        foodItemService.deleteFoodItem(id);

        return ResponseEntity.noContent().build();
    }

    // 6. Search food by category
    @Operation(
    summary = "Get food items by category",
    description = "Retrieves food items belonging to a specified category."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Food items retrieved successfully")
})
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FoodItem>> getFoodByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                foodItemService.getFoodItemsByCategory(category));
    }

    // 7. Search food by name
    @Operation(
    summary = "Search food items by name",
    description = "Searches the inventory for food items whose names contain the specified text."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
})
    @GetMapping("/search")
    public ResponseEntity<List<FoodItem>> searchFoodByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                foodItemService.searchFoodItemsByName(name));
    }

    // 8. Find expired or expiring food before a given date
    @Operation(
    summary = "Get food items expiring before a date",
    description = "Retrieves food items whose expiry date is on or before the specified date."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Expiring food items retrieved successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid date format")
})
    @GetMapping("/expiring")
    public ResponseEntity<List<FoodItem>> getFoodExpiringBefore(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                foodItemService.getFoodItemsExpiringBefore(date));
    }

    // 9. Find food expiring within a date range
    @Operation(
    summary = "Get food items expiring within a date range",
    description = "Retrieves food items whose expiry dates fall between the specified start and end dates."
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Food items retrieved successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid date format or date range")
})
    @GetMapping("/expiring-between")
    public ResponseEntity<List<FoodItem>> getFoodExpiringBetween(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                foodItemService.getFoodItemsExpiringBetween(
                        startDate, endDate));
    }
}
