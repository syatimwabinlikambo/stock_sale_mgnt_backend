package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;

    private Long saleId;

    private String receiptNumber;

    private Long customerId;

    private String customerName;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private String reference;

    private String notes;

    private LocalDateTime paymentDate;

    private BigDecimal saleTotal;

    private BigDecimal paidAmount;

    private BigDecimal balance;

    private String saleStatus;
}