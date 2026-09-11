package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class RequisitionRequest {

    private Long productId;

    private String requestedProductName;

    private String requestedCategory;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal quantity;

    private String unit;

    @DecimalMin("0.00")
    private BigDecimal estimatedUnitPrice;

    @NotBlank
    @Size(max = 500)
    private String reason;
}