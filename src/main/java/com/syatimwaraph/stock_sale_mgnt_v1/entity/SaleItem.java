package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItem {

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
     * PRODUCT
     * ============================================================
     */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    /*
     * ============================================================
     * QUANTITY
     * ============================================================
     */

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal quantity;


    /*
     * ============================================================
     * UNIT PRICE
     * ============================================================
     */

    @Column(
            name = "unit_price",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal unitPrice;


    /*
     * ============================================================
     * SUBTOTAL
     * ============================================================
     */

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal subtotal;
}