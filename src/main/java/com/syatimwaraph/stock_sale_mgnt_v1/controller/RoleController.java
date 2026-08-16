package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @GetMapping
    public ResponseEntity<List<Role>> getRoles() {

        return ResponseEntity.ok(
                Arrays.asList(Role.values())
        );
    }
}