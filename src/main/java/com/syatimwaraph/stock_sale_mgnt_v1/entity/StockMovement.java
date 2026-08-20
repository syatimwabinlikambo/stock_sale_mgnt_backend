package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.StockMovementType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockMovementType movementType;


    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;


    @Column(precision = 15, scale = 2)
    private BigDecimal previousStock;


    @Column(precision = 15, scale = 2)
    private BigDecimal newStock;


    @Column(length = 255)
    private String reason;


    @Column(length = 255)
    private String reference;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public StockMovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(StockMovementType movementType) {
        this.movementType = movementType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPreviousStock() {
        return previousStock;
    }

    public void setPreviousStock(BigDecimal previousStock) {
        this.previousStock = previousStock;
    }

    public BigDecimal getNewStock() {
        return newStock;
    }

    public void setNewStock(BigDecimal newStock) {
        this.newStock = newStock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}