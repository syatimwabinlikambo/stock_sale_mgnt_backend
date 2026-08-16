package com.syatimwaraph.stock_sale_mgnt_v1.dto;


import com.syatimwaraph.stock_sale_mgnt_v1.enums.Role;

public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean enabled;

    public UserResponse() {
    }

    public UserResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            Role role,
            boolean enabled
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}