package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.StockMovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class StockMovementRequest {

    @NotNull
    private Long productId;


    @NotNull
    private StockMovementType movementType;


    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal quantity;


    @Size(max = 255)
    private String reason;


    @Size(max = 255)
    private String reference;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public StockMovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(
            StockMovementType movementType
    ) {
        this.movementType = movementType;
    }


    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(
            BigDecimal quantity
    ) {
        this.quantity = quantity;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(
            String reason
    ) {
        this.reason = reason;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(
            String reference
    ) {
        this.reference = reference;
    }

}