package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class SaleRequest {

    @NotNull(message = "Customer is required")
    private Long customerId;


    @NotEmpty(message = "Sale must contain at least one item")
    @Valid
    private List<SaleItemRequest> items =
            new ArrayList<>();


    @NotNull(message = "Discount percentage is required")
    @DecimalMin(
            value = "0.00",
            message = "Discount cannot be negative"
    )
    private BigDecimal discountPercentage =
            BigDecimal.ZERO;

    private PaymentRequest payment;


    @Size(
            max = 500,
            message = "Notes cannot exceed 500 characters"
    )
    private String notes;


    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    public SaleRequest() {
    }


    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(
            List<SaleItemRequest> items
    ) {
        this.items = items;
    }


    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(
            BigDecimal discountPercentage
    ) {
        this.discountPercentage =
                discountPercentage;
    }


    public PaymentRequest getPayment() {
        return payment;
    }

    public void setPayment(
            PaymentRequest payment
    ) {
        this.payment = payment;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}










































