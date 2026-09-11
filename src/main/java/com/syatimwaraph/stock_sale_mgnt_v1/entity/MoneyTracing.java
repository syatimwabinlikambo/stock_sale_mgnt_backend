package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyDecision;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyTracingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "money_tracing")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTracing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;


    @Column(nullable = false, length = 250)
    private String purpose;


    @Column(length = 100)
    private String reference;


    @Column(length = 500)
    private String notes;


    // ============================================================
    // MANAGER
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submitted_by_id", nullable = false)
    private AppUser submittedBy;


    @Column(nullable = false)
    private LocalDateTime submittedAt;


    // ============================================================
    // CASHIER
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_id")
    private AppUser cashier;


    @Enumerated(EnumType.STRING)
    @Column(name = "cashier_decision", length = 20)
    private MoneyDecision cashierDecision;


    @Column(name = "cashier_comment", length = 500)
    private String cashierComment;


    @Column(name = "cashier_processed_at")
    private LocalDateTime cashierProcessedAt;


    // ============================================================
    // ADMIN
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private AppUser admin;


    @Enumerated(EnumType.STRING)
    @Column(name = "admin_decision", length = 20)
    private MoneyDecision adminDecision;


    @Column(name = "admin_comment", length = 500)
    private String adminComment;


    @Column(name = "admin_processed_at")
    private LocalDateTime adminProcessedAt;


    // ============================================================
    // STATUS
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private MoneyTracingStatus status =
            MoneyTracingStatus.PENDING_CASHIER;


    @PrePersist
    protected void onCreate() {

        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = MoneyTracingStatus.PENDING_CASHIER;
        }
    }
}