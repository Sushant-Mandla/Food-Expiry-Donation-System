
package com.fooddonation.expirymonitoring.controller;

import com.fooddonation.expirymonitoring.dto.ExpiryFoodItemDto;
import com.fooddonation.expirymonitoring.enums.ExpiryStatus;
import com.fooddonation.expirymonitoring.service.ExpiryMonitoringService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpiryMonitoringController.class)
class ExpiryMonitoringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpiryMonitoringService expiryMonitoringService;

    private ExpiryFoodItemDto createFoodItem(
            Long id, String name, ExpiryStatus status, long daysRemaining) {

        ExpiryFoodItemDto item = new ExpiryFoodItemDto();
        item.setId(id);
        item.setFoodName(name);
        item.setCategory("Vegetables");
        item.setQuantity(5.0);
        item.setUnit("kg");
        item.setExpiryDate(LocalDate.now().plusDays(daysRemaining));
        item.setLocation("Bengaluru");
        item.setProviderName("Test Provider");
        item.setProviderType("RESTAURANT");
        item.setExpiryStatus(status);
        item.setDaysRemaining(daysRemaining);

        return item;
    }

    @Test
    void shouldReturnAllExpiryStatuses() throws Exception {

        ExpiryFoodItemDto item = createFoodItem(
                1L, "Fresh Tomatoes", ExpiryStatus.SAFE, 5);

        when(expiryMonitoringService.getAllFoodItemsWithExpiryStatus())
                .thenReturn(List.of(item));

        mockMvc.perform(get("/api/expiry/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName")
                        .value("Fresh Tomatoes"))
                .andExpect(jsonPath("$[0].expiryStatus")
                        .value("SAFE"))
                .andExpect(jsonPath("$[0].daysRemaining").value(5));
    }

    @Test
    void shouldReturnExpiredFoodItems() throws Exception {

        ExpiryFoodItemDto item = createFoodItem(
                2L, "Old Tomatoes", ExpiryStatus.EXPIRED, -2);

        when(expiryMonitoringService.getExpiredFoodItems())
                .thenReturn(List.of(item));

        mockMvc.perform(get("/api/expiry/expired"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName")
                        .value("Old Tomatoes"))
                .andExpect(jsonPath("$[0].expiryStatus")
                        .value("EXPIRED"));
    }

    @Test
    void shouldReturnFoodExpiringSoon() throws Exception {

        ExpiryFoodItemDto item = createFoodItem(
                3L, "Fresh Milk", ExpiryStatus.EXPIRING_SOON, 2);

        when(expiryMonitoringService.getExpiringSoonFoodItems())
                .thenReturn(List.of(item));

        mockMvc.perform(get("/api/expiry/expiring-soon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName")
                        .value("Fresh Milk"))
                .andExpect(jsonPath("$[0].expiryStatus")
                        .value("EXPIRING_SOON"));
    }

    @Test
    void shouldReturnSafeFoodItems() throws Exception {

        ExpiryFoodItemDto item = createFoodItem(
                4L, "Fresh Apples", ExpiryStatus.SAFE, 7);

        when(expiryMonitoringService.getSafeFoodItems())
                .thenReturn(List.of(item));

        mockMvc.perform(get("/api/expiry/safe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].foodName")
                        .value("Fresh Apples"))
                .andExpect(jsonPath("$[0].expiryStatus")
                        .value("SAFE"));
    }

    @Test
    void shouldReturnEmptyListWhenNoExpiredFoodExists() throws Exception {

        when(expiryMonitoringService.getExpiredFoodItems())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/expiry/expired"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
