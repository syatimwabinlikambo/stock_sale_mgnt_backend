package com.syatimwaraph.stock_sale_mgnt_v1.repositories;

import com.syatimwaraph.stock_sale_mgnt_v1.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {


    // ============================================================
    // FIND BY EMAIL
    // ============================================================

    Optional<Customer> findByEmailIgnoreCase(
            String email
    );


    // ============================================================
    // CHECK EMAIL
    // ============================================================

    boolean existsByEmailIgnoreCase(
            String email
    );


    // ============================================================
    // SEARCH ALL CUSTOMERS
    // ============================================================

    Page<Customer>
    findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String firstName,
            String lastName,
            String phone,
            Pageable pageable
    );


    // ============================================================
    // FIND BY ACTIVE STATUS
    // ============================================================

    Page<Customer> findByActive(
            Boolean active,
            Pageable pageable
    );


    // ============================================================
    // SEARCH BY ACTIVE STATUS
    // ============================================================

    Page<Customer>
    findByActiveAndFirstNameContainingIgnoreCaseOrActiveAndLastNameContainingIgnoreCaseOrActiveAndPhoneContainingIgnoreCase(
            Boolean active1,
            String firstName,
            Boolean active2,
            String lastName,
            Boolean active3,
            String phone,
            Pageable pageable
    );
}