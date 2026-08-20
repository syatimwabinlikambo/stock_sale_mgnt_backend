package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.PaymentMethod;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * ============================================================
     * SALE
     * ============================================================
     */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;


    /*
     * ============================================================
     * AMOUNT
     * ============================================================
     */

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;


    /*
     * ============================================================
     * PAYMENT METHOD
     * ============================================================
     */

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_method",
            nullable = false,
            length = 30
    )
    private PaymentMethod paymentMethod;


    /*
     * ============================================================
     * REFERENCE
     * ============================================================
     */

    @Column(length = 100)
    private String reference;


    /*
     * ============================================================
     * PAYMENT DATE
     * ============================================================
     */

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;


    /*
     * ============================================================
     * NOTES
     * ============================================================
     */

    @Column(length = 255)
    private String notes;


    @PrePersist
    protected void onCreate() {

        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }
}