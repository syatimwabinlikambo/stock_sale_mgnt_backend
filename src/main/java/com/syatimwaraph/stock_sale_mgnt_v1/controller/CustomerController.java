package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.CustomerRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.CustomerResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.service.CustomerService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService customerService;


    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CustomerRequest request
    ) {

        CustomerResponse response =
                customerService.create(request);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAll(

            @RequestParam(
                    required = false
            )
            Boolean active,

            @PageableDefault(
                    size = 20,
                    sort = "lastName",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable

    ) {

        return ResponseEntity.ok(

                customerService.getAll(
                        active,
                        pageable
                )

        );
    }


    // ============================================================
    // SEARCH
    // ============================================================

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponse>> search(

            @RequestParam(
                    required = false
            )
            String keyword,

            @RequestParam(
                    required = false
            )
            Boolean active,

            @PageableDefault(
                    size = 20,
                    sort = "lastName",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable

    ) {

        return ResponseEntity.ok(

                customerService.search(
                        keyword,
                        active,
                        pageable
                )

        );
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                customerService.getById(id)
        );
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(

            @PathVariable Long id,

            @Valid
            @RequestBody
            CustomerRequest request

    ) {

        return ResponseEntity.ok(

                customerService.update(
                        id,
                        request
                )

        );
    }


    // ============================================================
    // DEACTIVATE
    // ============================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                customerService.deactivate(id)
        );
    }


    // ============================================================
    // ACTIVATE
    // ============================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                customerService.activate(id)
        );
    }
}