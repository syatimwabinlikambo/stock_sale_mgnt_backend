package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyDecision;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyTracingStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class MoneyTracingResponse {

    private Long id;

    private BigDecimal amount;

    private String purpose;

    private String reference;

    private String notes;


    // Manager

    private Long submittedById;

    private String submittedByName;

    private LocalDateTime submittedAt;


    // Cashier

    private Long cashierId;

    private String cashierName;

    private MoneyDecision cashierDecision;

    private String cashierComment;

    private LocalDateTime cashierProcessedAt;


    // Admin

    private Long adminId;

    private String adminName;

    private MoneyDecision adminDecision;

    private String adminComment;

    private LocalDateTime adminProcessedAt;


    // Workflow

    private MoneyTracingStatus status;
}