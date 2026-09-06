package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class SalePaymentRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

    @Size(max = 255)
    private String reference;

    @Size(max = 255)
    private String notes;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            PaymentMethod paymentMethod
    ) {
        this.paymentMethod = paymentMethod;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}