package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.MoneyTracingDecisionRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.MoneyTracingRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.MoneyTracingResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.service.MoneyTracingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/money-tracing")
@RequiredArgsConstructor
public class MoneyTracingController {

    private final MoneyTracingService moneyTracingService;


    // ============================================================
    // MANAGER — CREATE
    // ============================================================

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MoneyTracingResponse> create(
            @Valid @RequestBody MoneyTracingRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();

        MoneyTracingResponse response =
                moneyTracingService.create(
                        request,
                        email
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'CASHIER', 'ADMIN')")
    public ResponseEntity<List<MoneyTracingResponse>> getAll() {

        return ResponseEntity.ok(
                moneyTracingService.getAll()
        );
    }


    // ============================================================
    // PENDING CASHIER
    // ============================================================

    @GetMapping("/pending-cashier")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<List<MoneyTracingResponse>>
    getPendingCashier() {

        return ResponseEntity.ok(
                moneyTracingService.getPendingCashier()
        );
    }


    // ============================================================
    // PENDING ADMIN
    // ============================================================

    @GetMapping("/pending-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MoneyTracingResponse>>
    getPendingAdmin() {

        return ResponseEntity.ok(
                moneyTracingService.getPendingAdmin()
        );
    }


    // ============================================================
    // GET ONE
    // ============================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CASHIER', 'ADMIN')")
    public ResponseEntity<MoneyTracingResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                moneyTracingService.getById(id)
        );
    }


    // ============================================================
    // CASHIER DECISION
    // ============================================================

    @PutMapping("/{id}/cashier-decision")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<MoneyTracingResponse>
    cashierDecision(

            @PathVariable Long id,

            @Valid
            @RequestBody
            MoneyTracingDecisionRequest request,

            Authentication authentication) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                moneyTracingService.cashierDecision(
                        id,
                        request,
                        email
                )
        );
    }


    // ============================================================
    // ADMIN DECISION
    // ============================================================

    @PutMapping("/{id}/admin-decision")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MoneyTracingResponse>
    adminDecision(

            @PathVariable Long id,

            @Valid
            @RequestBody
            MoneyTracingDecisionRequest request,

            Authentication authentication) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                moneyTracingService.adminDecision(
                        id,
                        request,
                        email
                )
        );
    }
}