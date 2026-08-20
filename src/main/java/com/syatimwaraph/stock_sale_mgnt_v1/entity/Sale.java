package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * ============================================================
     * CUSTOMER
     * ============================================================
     */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    /*
     * ============================================================
     * SALE DATE
     * ============================================================
     */

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;


    /*
     * ============================================================
     * Sale financial section
     * ============================================================
     */

    @Column(
            name = "subtotal_amount",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal subtotalAmount;


    @Column(
            name = "discount_percentage",
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal discountPercentage;


    @Column(
            name = "discount_amount",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal discountAmount;


    @Column(
            name = "total_amount",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal totalAmount;


    @Column(
            name = "paid_amount",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal paidAmount;


    @Column(
            name = "balance",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balance;

    /*
     * ============================================================
     * STATUS
     * ============================================================
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SaleStatus status;


    /*
     * ============================================================
     * NOTES
     * ============================================================
     */

    @Column(length = 500)
    private String notes;


    /*
     * ============================================================
     * ITEMS
     * ============================================================
     */

    @OneToMany(
            mappedBy = "sale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SaleItem> items = new ArrayList<>();


    /*
     * ============================================================
     * PAYMENTS
     * ============================================================
     */

    @OneToMany(
            mappedBy = "sale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Payment> payments = new ArrayList<>();


    /*
     * ============================================================
     * TIMESTAMPS
     * ============================================================
     */

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {

        saleDate = saleDate == null
                ? LocalDateTime.now()
                : saleDate;

        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();

        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }

        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}
