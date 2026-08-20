package com.syatimwaraph.stock_sale_mgnt_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_customer_phone", columnList = "phone"),
                @Index(name = "idx_customer_email", columnList = "email"),
                @Index(name = "idx_customer_name", columnList = "last_name, first_name"),
                @Index(name = "idx_customer_active", columnList = "active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;


    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;


    @Column(length = 30)
    private String phone;


    @Column(length = 150)
    private String email;


    @Column(length = 255)
    private String address;


    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (active == null) {
            active = true;
        }
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}