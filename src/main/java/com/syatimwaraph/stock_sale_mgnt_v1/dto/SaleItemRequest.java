package com.syatimwaraph.stock_sale_mgnt_v1.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SaleItemRequest {

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(
            value = "0.01",
            message = "Quantity must be greater than zero"
    )
    private BigDecimal quantity;


    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    public SaleItemRequest() {
    }


    public SaleItemRequest(
            Long productId,
            BigDecimal quantity
    ) {
        this.productId = productId;
        this.quantity = quantity;
    }


    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}