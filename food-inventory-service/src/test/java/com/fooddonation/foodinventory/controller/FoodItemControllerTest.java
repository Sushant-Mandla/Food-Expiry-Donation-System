
package com.fooddonation.foodinventory.controller;

import com.fooddonation.foodinventory.entity.FoodItem;
import com.fooddonation.foodinventory.service.FoodItemService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddonation.foodinventory.exception.ResourceNotFoundException;
import com.fooddonation.foodinventory.exception.GlobalExceptionHandler;

import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodItemController.class)
@Import(GlobalExceptionHandler.class)
class FoodItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FoodItemService foodItemService;

    
private FoodItem createFoodItem() {

    FoodItem food = new FoodItem();

    food.setFoodName("Fresh Tomatoes");
    food.setCategory("Vegetables");
    food.setQuantity(50.0);
    food.setUnit("kg");
    food.setExpiryDate(LocalDate.of(2026, 11, 20));
    food.setLocation("Bangalore");
    food.setProviderName("Fresh Mart Supermarket");
    food.setProviderType("SUPERMARKET");

    return food;
}


    // 1. Add food item
    @Test
    void testAddFoodItem() throws Exception {

        FoodItem food = createFoodItem();

        when(foodItemService.addFoodItem(any(FoodItem.class)))
                .thenReturn(food);

        mockMvc.perform(post("/api/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.foodName").value("Fresh Tomatoes"))
                .andExpect(jsonPath("$.category").value("Vegetables"));

        verify(foodItemService).addFoodItem(any(FoodItem.class));
    }

    // 2. Get all food items
    @Test
    void testGetAllFoodItems() throws Exception {

        when(foodItemService.getAllFoodItems())
                .thenReturn(List.of(createFoodItem()));

        mockMvc.perform(get("/api/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName").value("Fresh Tomatoes"));

        verify(foodItemService).getAllFoodItems();
    }

    // 3. Get food item by ID
    @Test
    void testGetFoodItemById() throws Exception {

        when(foodItemService.getFoodItemById(1L))
                .thenReturn(createFoodItem());

        mockMvc.perform(get("/api/food/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foodName").value("Fresh Tomatoes"));

        verify(foodItemService).getFoodItemById(1L);
    }

    // 4. Update food item
    @Test
    void testUpdateFoodItem() throws Exception {

        FoodItem food = createFoodItem();
        food.setQuantity(40.0);

        when(foodItemService.updateFoodItem(eq(1L), any(FoodItem.class)))
                .thenReturn(food);

        mockMvc.perform(put("/api/food/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(40.0));

        verify(foodItemService).updateFoodItem(eq(1L), any(FoodItem.class));
    }

    // 5. Delete food item
    @Test
    void testDeleteFoodItem() throws Exception {

        doNothing().when(foodItemService).deleteFoodItem(1L);

        mockMvc.perform(delete("/api/food/1"))
                .andExpect(status().isNoContent());

        verify(foodItemService).deleteFoodItem(1L);
    }

    // 6. Search by category
    @Test
    void testGetFoodByCategory() throws Exception {

        when(foodItemService.getFoodItemsByCategory("Vegetables"))
                .thenReturn(List.of(createFoodItem()));

        mockMvc.perform(get("/api/food/category/Vegetables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Vegetables"));

        verify(foodItemService).getFoodItemsByCategory("Vegetables");
    }

    // 7. Search by food name
    @Test
    void testSearchFoodByName() throws Exception {

        when(foodItemService.searchFoodItemsByName("Tomatoes"))
                .thenReturn(List.of(createFoodItem()));

        mockMvc.perform(get("/api/food/search")
                .param("name", "Tomatoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName").value("Fresh Tomatoes"));

        verify(foodItemService).searchFoodItemsByName("Tomatoes");
    }

    // 8. Find expiring food
    @Test
    void testGetFoodExpiringBefore() throws Exception {

        LocalDate date = LocalDate.of(2026, 11, 25);

        when(foodItemService.getFoodItemsExpiringBefore(date))
                .thenReturn(List.of(createFoodItem()));

        mockMvc.perform(get("/api/food/expiring")
                .param("date", "2026-11-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName").value("Fresh Tomatoes"));

        verify(foodItemService).getFoodItemsExpiringBefore(date);
    }

    // 9. Find food between dates
    @Test
    void testGetFoodExpiringBetween() throws Exception {

        LocalDate startDate = LocalDate.of(2026, 11, 15);
        LocalDate endDate = LocalDate.of(2026, 11, 25);

        when(foodItemService.getFoodItemsExpiringBetween(startDate, endDate))
                .thenReturn(List.of(createFoodItem()));

        mockMvc.perform(get("/api/food/expiring-between")
                .param("startDate", "2026-11-15")
                .param("endDate", "2026-11-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName").value("Fresh Tomatoes"));

        verify(foodItemService)
                .getFoodItemsExpiringBetween(startDate, endDate);
    }

    // 10. Test resource not found exception (404)
    @Test
    void testFoodItemNotFound() throws Exception {

        when(foodItemService.getFoodItemById(999L))
                .thenThrow(new ResourceNotFoundException(
                        "Food item not found with ID: 999"));

        mockMvc.perform(get("/api/food/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Food item not found with ID: 999"));

        verify(foodItemService).getFoodItemById(999L);
    }

    // 11. Test blank food name validation (400)
    @Test
    void testAddFoodItemWithBlankName() throws Exception {

        FoodItem food = createFoodItem();
        food.setFoodName("");

        mockMvc.perform(post("/api/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.foodName")
                        .value("Food name is required"));

        verify(foodItemService, never()).addFoodItem(any(FoodItem.class));
    }

    // 12. Test negative quantity validation (400)
    @Test
    void testAddFoodItemWithNegativeQuantity() throws Exception {

        FoodItem food = createFoodItem();
        food.setQuantity(-10.0);

        mockMvc.perform(post("/api/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages.quantity")
                        .value("Quantity must be greater than zero"));

        verify(foodItemService, never()).addFoodItem(any(FoodItem.class));
    }

    // 13. Test missing required fields (400)
    @Test
    void testAddFoodItemWithMissingFields() throws Exception {

        String invalidJson = "{}";

        mockMvc.perform(post("/api/food")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.foodName")
                        .value("Food name is required"))
                .andExpect(jsonPath("$.messages.category")
                        .value("Category is required"))
                .andExpect(jsonPath("$.messages.quantity")
                        .value("Quantity is required"));

        verify(foodItemService, never()).addFoodItem(any(FoodItem.class));
    }

}
