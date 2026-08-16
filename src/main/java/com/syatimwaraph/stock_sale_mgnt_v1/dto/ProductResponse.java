package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;

    private String productName;

    private ProductCategory productCategory;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private BigDecimal stock;

    private String unit;

    private BigDecimal alertStock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public ProductResponse() {
    }


    public ProductResponse(
            Long id,
            String productName,
            ProductCategory productCategory,
            BigDecimal purchasePrice,
            BigDecimal sellingPrice,
            BigDecimal stock,
            String unit,
            BigDecimal alertStock,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        this.id = id;
        this.productName = productName;
        this.productCategory = productCategory;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stock = stock;
        this.unit = unit;
        this.alertStock = alertStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // ========================================================
    // GETTERS
    // ========================================================

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getAlertStock() {
        return alertStock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}