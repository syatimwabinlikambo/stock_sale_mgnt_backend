package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.Role;

public class AuthResponse {

    private String token;
    private String type;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public AuthResponse(
            String token,
            Long userId,
            String firstName,
            String lastName,
            String email,
            Role role
    ) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}