package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.StockMovementRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.StockMovement;
import com.syatimwaraph.stock_sale_mgnt_v1.service.StockService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;


    public StockController(
            StockService stockService
    ) {

        this.stockService =
                stockService;
    }
    /*
     * ============================================================
     * CREATE STOCK MOVEMENT
     * ============================================================
     */
    @PostMapping("/movements")
    public ResponseEntity<StockMovement> createMovement(

            @Valid
            @RequestBody
            StockMovementRequest request

    ) {

        StockMovement movement =
                stockService.createMovement(
                        request
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movement);
    }
    /*
     * ============================================================
     * GET ALL STOCK MOVEMENTS
     * ============================================================
     */

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovement>> getAllMovements() {

        return ResponseEntity.ok(
                stockService.getAllMovements()
        );
    }
    /*
     * ============================================================
     * GET MOVEMENTS FOR PRODUCT
     * ============================================================
     */

    @GetMapping("/movements/product/{productId}")
    public ResponseEntity<List<StockMovement>>
    getProductMovements(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                stockService.getProductMovements(
                        productId
                )
        );
    }
    /*
     * ============================================================
     * GET LOW STOCK PRODUCTS
     * ============================================================
     */
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockProducts() {

        return ResponseEntity.ok(
                stockService.getLowStockProducts()
        );
    }
    /*
     * ============================================================
     * GET OUT OF STOCK PRODUCTS
     * ============================================================
     */

    @GetMapping("/out-of-stock")
    public ResponseEntity<?> getOutOfStockProducts() {

        return ResponseEntity.ok(
                stockService.getOutOfStockProducts()
        );
    }

}