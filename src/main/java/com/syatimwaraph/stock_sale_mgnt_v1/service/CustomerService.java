package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.CustomerRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.CustomerResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Customer;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {


    private final CustomerRepository customerRepository;


    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    public CustomerResponse create(
            CustomerRequest request
    ) {

        Customer customer =
                Customer.builder()

                        .firstName(
                                request.getFirstName().trim()
                        )

                        .lastName(
                                request.getLastName().trim()
                        )

                        .phone(
                                normalize(
                                        request.getPhone()
                                )
                        )

                        .email(
                                normalize(
                                        request.getEmail()
                                )
                        )

                        .address(
                                normalize(
                                        request.getAddress()
                                )
                        )

                        .build();


        Customer saved =
                customerRepository.save(customer);


        return mapToResponse(saved);
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public CustomerResponse getById(
            Long id
    ) {

        Customer customer =
                customerRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: " + id
                                )
                        );


        return mapToResponse(customer);
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAll(
            Pageable pageable
    ) {

        return customerRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }


    // ============================================================
    // GET ALL BY STATUS
    // ============================================================

    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAll(
            Boolean active,
            Pageable pageable
    ) {

        // No status filter
        if (active == null) {

            return getAll(pageable);
        }


        return customerRepository
                .findByActive(
                        active,
                        pageable
                )
                .map(this::mapToResponse);
    }


    // ============================================================
    // SEARCH
    // ============================================================

    @Transactional(readOnly = true)
    public Page<CustomerResponse> search(
            String keyword,
            Pageable pageable
    ) {

        if (
                keyword == null ||
                        keyword.isBlank()
        ) {

            return getAll(pageable);
        }


        String value =
                keyword.trim();


        return customerRepository

                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        value,
                        value,
                        value,
                        pageable
                )

                .map(this::mapToResponse);
    }


    // ============================================================
    // SEARCH BY STATUS
    // ============================================================

    @Transactional(readOnly = true)
    public Page<CustomerResponse> search(
            String keyword,
            Boolean active,
            Pageable pageable
    ) {

        /*
         * No keyword
         */
        if (
                keyword == null ||
                        keyword.isBlank()
        ) {

            return getAll(
                    active,
                    pageable
            );
        }


        String value =
                keyword.trim();


        /*
         * No status filter
         */
        if (active == null) {

            return search(
                    value,
                    pageable
            );
        }


        /*
         * Keyword + active/inactive filter
         */
        return customerRepository

                .findByActiveAndFirstNameContainingIgnoreCaseOrActiveAndLastNameContainingIgnoreCaseOrActiveAndPhoneContainingIgnoreCase(
                        active,
                        value,

                        active,
                        value,

                        active,
                        value,

                        pageable
                )

                .map(this::mapToResponse);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    public CustomerResponse update(
            Long id,
            CustomerRequest request
    ) {

        Customer customer =
                customerRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: " + id
                                )
                        );


        customer.setFirstName(
                request.getFirstName().trim()
        );


        customer.setLastName(
                request.getLastName().trim()
        );


        customer.setPhone(
                normalize(
                        request.getPhone()
                )
        );


        customer.setEmail(
                normalize(
                        request.getEmail()
                )
        );


        customer.setAddress(
                normalize(
                        request.getAddress()
                )
        );


        Customer updated =
                customerRepository.save(customer);


        return mapToResponse(updated);
    }


    // ============================================================
    // DEACTIVATION / ACTIVATION
    // ============================================================

    @Transactional
    public CustomerResponse setActive(
            Long id,
            boolean active
    ) {

        Customer customer =
                customerRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: " + id
                                )
                        );


        customer.setActive(active);


        Customer saved =
                customerRepository.save(customer);


        return mapToResponse(saved);
    }


    // ============================================================
    // DEACTIVATE
    // ============================================================

    @Transactional
    public CustomerResponse deactivate(
            Long id
    ) {

        return setActive(
                id,
                false
        );
    }


    // ============================================================
    // ACTIVATE
    // ============================================================

    @Transactional
    public CustomerResponse activate(
            Long id
    ) {

        return setActive(
                id,
                true
        );
    }


    // ============================================================
    // ENTITY → DTO
    // ============================================================

    private CustomerResponse mapToResponse(
            Customer customer
    ) {

        return CustomerResponse.builder()

                .id(
                        customer.getId()
                )

                .firstName(
                        customer.getFirstName()
                )

                .lastName(
                        customer.getLastName()
                )

                .phone(
                        customer.getPhone()
                )

                .email(
                        customer.getEmail()
                )

                .address(
                        customer.getAddress()
                )

                .active(
                        customer.getActive()
                )

                .createdAt(
                        customer.getCreatedAt()
                )

                .updatedAt(
                        customer.getUpdatedAt()
                )

                .build();
    }


    // ============================================================
    // NORMALIZE
    // ============================================================

    private String normalize(
            String value
    ) {

        if (
                value == null ||
                        value.isBlank()
        ) {

            return null;
        }


        return value.trim();
    }
}