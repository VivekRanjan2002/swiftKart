package com.app.swiftKart.dto;

import java.math.BigDecimal;

public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private String stockQuantity;
    private String category;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }



    public String getStockQuantity() {
        return stockQuantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }






}
