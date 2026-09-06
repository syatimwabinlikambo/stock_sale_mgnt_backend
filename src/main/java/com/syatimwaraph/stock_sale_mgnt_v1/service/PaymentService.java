package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.PaymentRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.PaymentResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Customer;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Payment;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Sale;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.BadRequestException;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.PaymentRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            SaleRepository saleRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.saleRepository = saleRepository;
    }

    // ============================================================
    // RECORD PAYMENT
    // ============================================================

    public PaymentResponse createPayment(
            PaymentRequest request
    ) {

        Sale sale =
                saleRepository.findById(
                        request.getSaleId()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Sale not found with id: "
                                        + request.getSaleId()
                        )
                );


        // --------------------------------------------------------
        // CANNOT PAY A CANCELLED SALE
        // --------------------------------------------------------

        if (sale.getStatus() == SaleStatus.CANCELLED) {

            throw new BadRequestException(
                    "Cannot make payment for a cancelled sale."
            );
        }

        // --------------------------------------------------------
        // CANNOT PAY AN ALREADY PAID SALE
        // --------------------------------------------------------

        if (sale.getBalance().compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new BadRequestException(
                    "This sale has already been fully paid."
            );
        }


        BigDecimal amount =
                request.getAmount();


        // --------------------------------------------------------
        // PAYMENT MUST BE POSITIVE
        // --------------------------------------------------------

        if (amount.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new BadRequestException(
                    "Payment amount must be greater than zero."
            );
        }


        // --------------------------------------------------------
        // PAYMENT CANNOT EXCEED BALANCE
        // --------------------------------------------------------

        if (amount.compareTo(
                sale.getBalance()
        ) > 0) {

            throw new BadRequestException(
                    "Payment amount cannot exceed the outstanding balance."
            );
        }

        // --------------------------------------------------------
        // CREATE PAYMENT
        // --------------------------------------------------------

        Payment payment =
                Payment.builder()

                        .sale(sale)

                        .amount(amount)

                        .paymentMethod(
                                request.getPaymentMethod()
                        )

                        .reference(
                                normalize(
                                        request.getReference()
                                )
                        )

                        .notes(
                                normalize(
                                        request.getNotes()
                                )
                        )

                        .build();


        Payment savedPayment =
                paymentRepository.save(payment);


        // --------------------------------------------------------
        // UPDATE SALE
        // --------------------------------------------------------

        BigDecimal currentPaid =
                sale.getPaidAmount() != null
                        ? sale.getPaidAmount()
                        : BigDecimal.ZERO;


        BigDecimal newPaid =
                currentPaid.add(amount);


        BigDecimal total =
                sale.getTotalAmount();


        BigDecimal newBalance =
                total.subtract(newPaid);


        // Prevent negative balance because of rounding
        if (newBalance.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            newBalance =
                    BigDecimal.ZERO;
        }


        sale.setPaidAmount(newPaid);

        sale.setBalance(newBalance);


        // --------------------------------------------------------
        // UPDATE SALE STATUS
        // --------------------------------------------------------

        if (newBalance.compareTo(
                BigDecimal.ZERO
        ) == 0) {

            sale.setStatus(
                    SaleStatus.PAID
            );

        } else {

            sale.setStatus(
                    SaleStatus.PARTIAL
            );
        }


        saleRepository.save(sale);


        return mapToResponse(
                savedPayment,
                sale
        );
    }

    // ============================================================
    // GET LATEST PAYMENTS
    // ============================================================

    @Transactional(readOnly = true)
    public List<PaymentResponse> getLatestPayments() {

        return paymentRepository
                .findTop20ByOrderByPaymentDateDesc()
                .stream()
                .map(payment ->
                        mapToResponse(
                                payment,
                                payment.getSale()
                        )
                )
                .toList();
    }


    // ============================================================
    // GET PAYMENTS FOR SALE
    // ============================================================

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsForSale(
            Long saleId
    ) {

        Sale sale =
                saleRepository.findById(
                        saleId
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Sale not found with id: "
                                        + saleId
                        )
                );


        return paymentRepository
                .findBySaleIdOrderByPaymentDateDesc(
                        saleId
                )
                .stream()
                .map(payment ->
                        mapToResponse(
                                payment,
                                sale
                        )
                )
                .toList();
    }


    // ============================================================
    // GET PAYMENT BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public PaymentResponse getById(
            Long id
    ) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Payment not found with id: "
                                                        + id
                                        )
                        );


        return mapToResponse(
                payment,
                payment.getSale()
        );
    }


    // ============================================================
    // MAPPING
    // ============================================================

    private PaymentResponse mapToResponse(
            Payment payment,
            Sale sale
    ) {

        Customer customer =
                sale.getCustomer();

        return PaymentResponse.builder()

                .id(payment.getId())

                .saleId(sale.getId())

                .receiptNumber(
                        sale.getReceiptNumber()
                )

                .customerId(
                        customer != null
                                ? customer.getId()
                                : null
                )

                .customerName(
                        customer != null
                                ? customer.getFirstName()
                                + " "
                                + customer.getLastName()
                                : "Walk-in Customer"
                )

                .amount(
                        payment.getAmount()
                )

                .paymentMethod(
                        payment.getPaymentMethod()
                )

                .reference(
                        payment.getReference()
                )

                .notes(
                        payment.getNotes()
                )

                .paymentDate(
                        payment.getPaymentDate()
                )

                .saleTotal(
                        sale.getTotalAmount()
                )

                .paidAmount(
                        sale.getPaidAmount()
                )

                .balance(
                        sale.getBalance()
                )

                .saleStatus(
                        sale.getStatus().name()
                )

                .build();
    }


    // ============================================================
    // NORMALIZE
    // ============================================================

    private String normalize(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}