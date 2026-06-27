package com.smartseats.demo.event.dto;

import java.math.BigDecimal;

public class SeatCategoryResponse {

    private Long id;
    private String name;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal price;

    public SeatCategoryResponse(Long id, String name,
                                 Integer totalSeats, Integer availableSeats,
                                 BigDecimal price) {
        this.id = id;
        this.name = name;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getTotalSeats() { return totalSeats; }
    public Integer getAvailableSeats() { return availableSeats; }
    public BigDecimal getPrice() { return price; }
}