package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.ProductCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'USER')"
    )
    public ResponseEntity<List<ProductCategory>>
    getCategories() {

        return ResponseEntity.ok(
                Arrays.asList(
                        ProductCategory.values()
                )
        );
    }
}