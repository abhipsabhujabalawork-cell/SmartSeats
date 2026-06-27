package com.smartseats.demo.event.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SeatCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Must have at least 1 seat")
    private Integer totalSeats;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    // Getters
    public String getName() { return name; }
    public Integer getTotalSeats() { return totalSeats; }
    public BigDecimal getPrice() { return price; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    public void setPrice(BigDecimal price) { this.price = price; }
}