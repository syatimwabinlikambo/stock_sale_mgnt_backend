package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponse {

    private Long id;

    private String receiptNumber;

    private LocalDateTime saleDate;

    private Long customerId;

    private String customerName;

    private String customerPhone;

    private List<SaleItemResponse> items;

    private BigDecimal subtotal;

    private BigDecimal discountPercentage;

    private BigDecimal discountAmount;

    private BigDecimal total;

    private BigDecimal paidAmount;

    private BigDecimal balance;

    private SaleStatus status;

    private PaymentResponse payment;

    private String notes;


    public SaleResponse() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(
            String receiptNumber
    ) {
        this.receiptNumber = receiptNumber;
    }


    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(
            LocalDateTime saleDate
    ) {
        this.saleDate = saleDate;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(
            String customerName
    ) {
        this.customerName = customerName;
    }


    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(
            String customerPhone
    ) {
        this.customerPhone = customerPhone;
    }


    public List<SaleItemResponse> getItems() {
        return items;
    }

    public void setItems(
            List<SaleItemResponse> items
    ) {
        this.items = items;
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(
            BigDecimal subtotal
    ) {
        this.subtotal = subtotal;
    }


    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(
            BigDecimal discountPercentage
    ) {
        this.discountPercentage = discountPercentage;
    }


    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(
            BigDecimal discountAmount
    ) {
        this.discountAmount = discountAmount;
    }


    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(
            BigDecimal total
    ) {
        this.total = total;
    }


    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(
            BigDecimal paidAmount
    ) {
        this.paidAmount = paidAmount;
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


    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(
            PaymentResponse payment
    ) {
        this.payment = payment;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}