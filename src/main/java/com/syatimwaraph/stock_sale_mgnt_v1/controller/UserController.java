package com.syatimwaraph.stock_sale_mgnt_v1.controller;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.AdminCreateUserRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.UserResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody
            AdminCreateUserRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userService.createUser(request)
                );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled
    ) {

        return ResponseEntity.ok(
                userService.updateStatus(
                        id,
                        enabled
                )
        );
    }
}