package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.ProductCategory;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(
                        name = "idx_product_name",
                        columnList = "product_name"
                ),
                @Index(
                        name = "idx_product_category",
                        columnList = "product_category"
                )
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "product_name",
            nullable = false,
            length = 150
    )
    private String productName;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "product_category",
            nullable = false,
            length = 50
    )
    private ProductCategory productCategory;


    @Column(
            name = "purchase_price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal purchasePrice;


    @Column(
            name = "selling_price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal sellingPrice;


    @Column(
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal stock = BigDecimal.ZERO;


    @Column(
            nullable = false,
            length = 30
    )
    private String unit;


    @Column(
            name = "alert_stock",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal alertStock = BigDecimal.valueOf(5);


    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }


    // ========================================================
    // GETTERS AND SETTERS
    // ========================================================

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(
            String productName
    ) {
        this.productName = productName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(
            ProductCategory productCategory
    ) {
        this.productCategory = productCategory;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(
            BigDecimal purchasePrice
    ) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(
            BigDecimal sellingPrice
    ) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(
            BigDecimal stock
    ) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(
            String unit
    ) {
        this.unit = unit;
    }

    public BigDecimal getAlertStock() {
        return alertStock;
    }

    public void setAlertStock(
            BigDecimal alertStock
    ) {
        this.alertStock = alertStock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}