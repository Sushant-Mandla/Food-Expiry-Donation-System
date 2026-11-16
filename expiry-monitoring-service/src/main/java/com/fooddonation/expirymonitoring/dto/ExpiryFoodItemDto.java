
package com.fooddonation.expirymonitoring.dto;

import com.fooddonation.expirymonitoring.enums.ExpiryStatus;

import java.time.LocalDate;

public class ExpiryFoodItemDto {

    private Long id;
    private String foodName;
    private String category;
    private Double quantity;
    private String unit;
    private LocalDate expiryDate;
    private String location;
    private String providerName;
    private String providerType;

    private ExpiryStatus expiryStatus;
    private long daysRemaining;

    public ExpiryFoodItemDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public ExpiryStatus getExpiryStatus() {
        return expiryStatus;
    }

    public void setExpiryStatus(ExpiryStatus expiryStatus) {
        this.expiryStatus = expiryStatus;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }
}
