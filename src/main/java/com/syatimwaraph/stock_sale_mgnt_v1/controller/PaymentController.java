package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.PaymentRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.PaymentResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.SaleResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }


    // ============================================================
    // CREATE PAYMENT
    // ============================================================

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(

            @Valid
            @RequestBody
            PaymentRequest request

    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        paymentService.createPayment(
                                request
                        )
                );
    }


    // ============================================================
    // GET PAYMENT
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                paymentService.getById(id)
        );
    }


    // ============================================================
    // GET PAYMENTS FOR SALE
    // ============================================================

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<PaymentResponse>>
    getPaymentsForSale(
            @PathVariable Long saleId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsForSale(
                        saleId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getLatestPayments() {

        return ResponseEntity.ok(
                paymentService.getLatestPayments()
        );
    }
}