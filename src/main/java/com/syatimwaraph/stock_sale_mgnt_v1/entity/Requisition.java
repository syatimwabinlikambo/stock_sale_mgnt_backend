package com.syatimwaraph.stock_sale_mgnt_v1.entity;


import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "requisitions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Existing product.
     * NULL when requesting a completely new product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /*
     * Used when the requested product doesn't exist yet.
     */
    @Column(name = "requested_product_name", length = 150)
    private String requestedProductName;

    @Column(name = "requested_category", length = 100)
    private String requestedCategory;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal quantity;

    @Column(length = 50)
    private String unit;

    @Column(name = "estimated_unit_price", precision = 19, scale = 2)
    private BigDecimal estimatedUnitPrice;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private RequisitionStatus status =
            RequisitionStatus.PENDING;

    /*
     * Person who submitted the requisition.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by_id", nullable = false)
    private AppUser requestedBy;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    /*
     * Approval information
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_id")
    private AppUser processedBy;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "processing_note", length = 500)
    private String processingNote;

    @PrePersist
    protected void onCreate() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
    }
}