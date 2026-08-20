package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;


    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;


    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    private String phone;


    @Email(message = "Invalid email address")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;


    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;
}