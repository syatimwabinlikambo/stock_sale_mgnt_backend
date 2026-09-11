package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionDecisionRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionStatus;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Product;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Requisition;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.BadRequestException;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.ProductRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.RequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository userRepository;

    public RequisitionResponse create(
            RequisitionRequest request,
            Long userId
    ) {

        AppUser user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        Product product = null;


        /*
         * EXISTING PRODUCT
         */
        if (request.getProductId() != null) {

            product =
                    productRepository.findById(
                            request.getProductId()
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id: "
                                            + request.getProductId()
                            )
                    );

        }
        /*
         * NEW PRODUCT
         */
        else {

            if (request.getRequestedProductName() == null
                    || request.getRequestedProductName().isBlank()) {

                throw new BadRequestException(
                        "Product name is required for a new product."
                );
            }

        }


        Requisition requisition =
                Requisition.builder()
                        .product(product)
                        .requestedProductName(
                                normalize(
                                        request.getRequestedProductName()
                                )
                        )
                        .requestedCategory(
                                normalize(
                                        request.getRequestedCategory()
                                )
                        )
                        .quantity(request.getQuantity())
                        .unit(
                                normalize(
                                        request.getUnit()
                                )
                        )
                        .estimatedUnitPrice(
                                request.getEstimatedUnitPrice()
                        )
                        .reason(
                                request.getReason().trim()
                        )
                        .status(
                                RequisitionStatus.PENDING
                        )
                        .requestedBy(user)
                        .build();


        return map(
                requisitionRepository.save(
                        requisition
                )
        );
    }


    @Transactional(readOnly = true)
    public List<RequisitionResponse> getAll() {

        return requisitionRepository
                .findAllByOrderByRequestedAtDesc()
                .stream()
                .map(this::map)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<RequisitionResponse> getPending() {

        return requisitionRepository
                .findByStatusOrderByRequestedAtDesc(
                        RequisitionStatus.PENDING
                )
                .stream()
                .map(this::map)
                .toList();
    }


    @Transactional(readOnly = true)
    public RequisitionResponse getById(Long id) {

        Requisition requisition =
                requisitionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Requisition not found with id: "
                                                + id
                                )
                        );

        return map(requisition);
    }


    public RequisitionResponse decide(
            Long id,
            RequisitionDecisionRequest request,
            Long userId
    ) {

        Requisition requisition =
                requisitionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Requisition not found with id: "
                                                + id
                                )
                        );


        if (requisition.getStatus()
                != RequisitionStatus.PENDING) {

            throw new BadRequestException(
                    "Only pending requisitions can be approved or denied."
            );
        }


        if (request.getStatus()
                != RequisitionStatus.APPROVED
                &&
                request.getStatus()
                        != RequisitionStatus.DENIED) {

            throw new BadRequestException(
                    "Invalid requisition decision."
            );
        }


        AppUser processor =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        requisition.setStatus(
                request.getStatus()
        );

        requisition.setProcessedBy(
                processor
        );

        requisition.setProcessedAt(
                LocalDateTime.now()
        );

        requisition.setProcessingNote(
                normalize(
                        request.getNote()
                )
        );


        return map(
                requisitionRepository.save(
                        requisition
                )
        );
    }


    private RequisitionResponse map(
            Requisition requisition
    ) {

        Product product =
                requisition.getProduct();

        AppUser requester =
                requisition.getRequestedBy();

        AppUser processor =
                requisition.getProcessedBy();


        BigDecimal estimatedTotal =
                null;


        if (requisition.getEstimatedUnitPrice() != null) {

            estimatedTotal =
                    requisition
                            .getEstimatedUnitPrice()
                            .multiply(
                                    requisition.getQuantity()
                            );
        }


        return RequisitionResponse.builder()

                .id(requisition.getId())

                .productId(
                        product != null
                                ? product.getId()
                                : null
                )

                .productName(
                        product != null
                                ? product.getProductName()
                                : requisition
                                .getRequestedProductName()
                )

                .newProduct(
                        product == null
                )

                .requestedProductName(
                        requisition
                                .getRequestedProductName()
                )

                .requestedCategory(
                        requisition
                                .getRequestedCategory()
                )

                .quantity(
                        requisition.getQuantity()
                )

                .unit(
                        requisition.getUnit()
                )

                .estimatedUnitPrice(
                        requisition
                                .getEstimatedUnitPrice()
                )

                .estimatedTotal(
                        estimatedTotal
                )

                .reason(
                        requisition.getReason()
                )

                .status(
                        requisition.getStatus()
                )

                .requestedById(
                        requester.getId()
                )

                .requestedByName(
                        requester.getFirstName()
                                + " "
                                + requester.getLastName()
                )

                .requestedAt(
                        requisition.getRequestedAt()
                )

                .processedById(
                        processor != null
                                ? processor.getId()
                                : null
                )

                .processedByName(
                        processor != null
                                ? processor.getFirstName()
                                + " "
                                + processor.getLastName()
                                : null
                )

                .processedAt(
                        requisition.getProcessedAt()
                )

                .processingNote(
                        requisition.getProcessingNote()
                )

                .build();
    }


    private String normalize(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}