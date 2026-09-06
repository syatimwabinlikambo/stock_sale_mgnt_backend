package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import java.math.BigDecimal;

public class SaleItemResponse {

    private Long productId;

    private String productName;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;


    public SaleItemResponse() {
    }


    public SaleItemResponse(
            Long productId,
            String productName,
            BigDecimal quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {

        this.productId = productId;

        this.productName = productName;

        this.quantity = quantity;

        this.unitPrice = unitPrice;

        this.subtotal = subtotal;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(
            String productName
    ) {
        this.productName = productName;
    }


    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(
            BigDecimal quantity
    ) {
        this.quantity = quantity;
    }


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(
            BigDecimal unitPrice
    ) {
        this.unitPrice = unitPrice;
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(
            BigDecimal subtotal
    ) {
        this.subtotal = subtotal;
    }
}