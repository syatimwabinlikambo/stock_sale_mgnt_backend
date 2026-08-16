package com.syatimwaraph.stock_sale_mgnt_v1.controller;


import com.syatimwaraph.stock_sale_mgnt_v1.dto.AdminCreateUserRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.AuthResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.LoginRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.SignupRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {

        AuthResponse response = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> createUser(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}