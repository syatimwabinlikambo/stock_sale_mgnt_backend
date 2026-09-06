package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.*;
import com.syatimwaraph.stock_sale_mgnt_v1.service.ReceiptService;
import com.syatimwaraph.stock_sale_mgnt_v1.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final ReceiptService receiptService;


    public SaleController(
            SaleService saleService, ReceiptService receiptService
    ) {
        this.saleService = saleService;
        this.receiptService = receiptService;
    }


    // ============================================================
    // CREATE SALE
    // ============================================================

    @PostMapping
    public ResponseEntity<ReceiptResponse> createSale(
            @Valid @RequestBody SaleRequest request
    ) {

        ReceiptResponse receipt =
                saleService.createSale(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(receipt);
    }


    // ============================================================
    // GET SALE BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                saleService.getSaleById(id)
        );
    }


    // ============================================================
    // GET RECEIPT
    // ============================================================

    @GetMapping("/{id}/receipt")
    public ResponseEntity<ReceiptResponse> getReceipt(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                receiptService.generateReceipt(id)
        );
    }


    // ============================================================
    // GET ALL SALES
    // ============================================================

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getAllSales(

            @PageableDefault(
                    size = 20,
                    sort = "saleDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable

    ) {

        return ResponseEntity.ok(
                saleService.getAllSales(pageable)
        );
    }


    // ============================================================
    // PAYMENT
    // ============================================================
    @PostMapping("/{saleId}/payments")
    public ResponseEntity<SalePaymentResponse> makePayment(

            @PathVariable Long saleId,

            @Valid
            @RequestBody
            SalePaymentRequest request

    ) {

        SalePaymentResponse response =
                saleService.makePayment(
                        saleId,
                        request
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/outstanding")
    public ResponseEntity<List<SaleResponse>>
    getOutstandingSales() {

        return ResponseEntity.ok(
                saleService.getOutstandingSales()
        );
    }
}