package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;

import java.math.BigDecimal;

public class SalePaymentResponse {

    private Long saleId;

    private String receiptNumber;

    private BigDecimal saleTotal;

    private BigDecimal previousPaidAmount;

    private BigDecimal paymentAmount;

    private BigDecimal totalPaidAmount;

    private BigDecimal balance;

    private SaleStatus status;


    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }


    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }


    public BigDecimal getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(BigDecimal saleTotal) {
        this.saleTotal = saleTotal;
    }


    public BigDecimal getPreviousPaidAmount() {
        return previousPaidAmount;
    }

    public void setPreviousPaidAmount(
            BigDecimal previousPaidAmount
    ) {
        this.previousPaidAmount =
                previousPaidAmount;
    }


    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(
            BigDecimal paymentAmount
    ) {
        this.paymentAmount =
                paymentAmount;
    }


    public BigDecimal getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(
            BigDecimal totalPaidAmount
    ) {
        this.totalPaidAmount =
                totalPaidAmount;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(
            BigDecimal balance
    ) {
        this.balance = balance;
    }


    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(
            SaleStatus status
    ) {
        this.status = status;
    }
}