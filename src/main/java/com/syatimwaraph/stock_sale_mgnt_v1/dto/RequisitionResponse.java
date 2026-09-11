package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class RequisitionResponse {

    private Long id;

    private Long productId;

    private String productName;

    private boolean newProduct;

    private String requestedProductName;

    private String requestedCategory;

    private BigDecimal quantity;

    private String unit;

    private BigDecimal estimatedUnitPrice;

    private BigDecimal estimatedTotal;

    private String reason;

    private RequisitionStatus status;

    private Long requestedById;

    private String requestedByName;

    private LocalDateTime requestedAt;

    private Long processedById;

    private String processedByName;

    private LocalDateTime processedAt;

    private String processingNote;
}