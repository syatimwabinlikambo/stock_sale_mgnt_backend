package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionDecisionRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.service.RequisitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requisitions")
@RequiredArgsConstructor
public class RequisitionController {

    private final RequisitionService requisitionService;
    private final AppUserRepository userRepository;

    @PostMapping
    public ResponseEntity<RequisitionResponse> create(
            @Valid @RequestBody RequisitionRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requisitionService.create(request, user.getId()));
    }


    @GetMapping
    public ResponseEntity<List<RequisitionResponse>> getAll() {

        return ResponseEntity.ok(
                requisitionService.getAll()
        );
    }


    @GetMapping("/pending")
    public ResponseEntity<List<RequisitionResponse>> getPending() {

        return ResponseEntity.ok(
                requisitionService.getPending()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<RequisitionResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                requisitionService.getById(id)
        );
    }


    @PutMapping("/{id}/decision")
    public ResponseEntity<RequisitionResponse> decide(
            @PathVariable Long id,
            @Valid @RequestBody RequisitionDecisionRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        return ResponseEntity.ok(
                requisitionService.decide(id, request, user.getId())
        );
    }
}