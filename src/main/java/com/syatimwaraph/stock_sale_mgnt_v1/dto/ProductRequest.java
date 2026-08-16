package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.ProductCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(
            max = 150,
            message = "Product name cannot exceed 150 characters"
    )
    private String productName;


    @NotNull(message = "Product category is required")
    private ProductCategory productCategory;


    @NotNull(message = "Purchase price is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Purchase price cannot be negative"
    )
    private BigDecimal purchasePrice;


    @NotNull(message = "Selling price is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Selling price cannot be negative"
    )
    private BigDecimal sellingPrice;


    @NotNull(message = "Stock is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Stock cannot be negative"
    )
    private BigDecimal stock;


    @NotBlank(message = "Unit is required")
    @Size(
            max = 30,
            message = "Unit cannot exceed 30 characters"
    )
    private String unit;


    @NotNull(message = "Alert stock is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Alert stock cannot be negative"
    )
    private BigDecimal alertStock;


    // ========================================================
    // GETTERS AND SETTERS
    // ========================================================

    public String getProductName() {
        return productName;
    }

    public void setProductName(
            String productName
    ) {
        this.productName = productName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(
            ProductCategory productCategory
    ) {
        this.productCategory = productCategory;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(
            BigDecimal purchasePrice
    ) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(
            BigDecimal sellingPrice
    ) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(
            BigDecimal stock
    ) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(
            String unit
    ) {
        this.unit = unit;
    }

    public BigDecimal getAlertStock() {
        return alertStock;
    }

    public void setAlertStock(
            BigDecimal alertStock
    ) {
        this.alertStock = alertStock;
    }
}