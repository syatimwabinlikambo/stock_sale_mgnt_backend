package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.ProductRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.ProductResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(
            ProductService productService
    ) {

        this.productService =
                productService;
    }


    // ========================================================
    // GET ALL
    // ========================================================

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'USER')"
    )
    public ResponseEntity<List<ProductResponse>>
    getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }


    // ========================================================
    // GET BY ID
    // ========================================================

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'USER')"
    )
    public ResponseEntity<ProductResponse>
    getProductById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }


    // ========================================================
    // CREATE
    // ========================================================

    @PostMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER')"
    )
    public ResponseEntity<ProductResponse>
    createProduct(
            @Valid
            @RequestBody
            ProductRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productService.createProduct(
                                request
                        )
                );
    }


    // ========================================================
    // UPDATE
    // ========================================================

    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER')"
    )
    public ResponseEntity<ProductResponse>
    updateProduct(
            @PathVariable Long id,

            @Valid
            @RequestBody
            ProductRequest request
    ) {

        return ResponseEntity.ok(
                productService.updateProduct(
                        id,
                        request
                )
        );
    }


    // ========================================================
    // DELETE
    // ========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    public ResponseEntity<Void>
    deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent()
                .build();
    }
}