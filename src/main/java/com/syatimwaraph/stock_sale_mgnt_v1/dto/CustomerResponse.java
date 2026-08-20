package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String address;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}