package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.*;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.*;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.StockMovementType;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.BadRequestException;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final PaymentRepository paymentRepository;

    private final StockMovementRepository stockMovementRepository;

    private final ReceiptNumberService receiptNumberService;


    /*
     * Maximum discount allowed.
     *
     * Example:
     *
     * sale.maximum-discount-percentage=10
     *
     */

    private final BigDecimal maximumDiscountPercentage;


    public SaleService(
            SaleRepository saleRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            PaymentRepository paymentRepository,
            StockMovementRepository stockMovementRepository, ReceiptNumberService receiptNumberService,
            @Value("${sale.maximum-discount-percentage:10}")
            BigDecimal maximumDiscountPercentage
    ) {

        this.saleRepository =
                saleRepository;

        this.customerRepository =
                customerRepository;

        this.productRepository =
                productRepository;

        this.paymentRepository =
                paymentRepository;

        this.stockMovementRepository =
                stockMovementRepository;
        this.receiptNumberService = receiptNumberService;

        this.maximumDiscountPercentage =
                maximumDiscountPercentage;
    }


    // ============================================================
    // CREATE SALE
    // ============================================================
    @Transactional
    public ReceiptResponse createSale(
            SaleRequest request
    ) {

        /*
         * ============================================================
         * 1. VALIDATE CUSTOMER
         * ============================================================
         */

        Customer customer =
                customerRepository.findById(
                        request.getCustomerId()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Customer not found with id: "
                                        + request.getCustomerId()
                        )
                );


        /*
         * ------------------------------------------------------------
         * Do not allow inactive customers
         * to make new sales.
         * ------------------------------------------------------------
         */

        if (!customer.getActive()) {

            throw new BadRequestException(
                    "Customer is inactive"
            );
        }


        /*
         * ============================================================
         * 2. VALIDATE DISCOUNT
         * ============================================================
         */

        BigDecimal discountPercentage =
                request.getDiscountPercentage();


        if (discountPercentage == null) {

            discountPercentage =
                    BigDecimal.ZERO;
        }


        if (
                discountPercentage.compareTo(
                        BigDecimal.ZERO
                ) < 0
        ) {

            throw new BadRequestException(
                    "Discount cannot be negative"
            );
        }


        if (
                discountPercentage.compareTo(
                        maximumDiscountPercentage
                ) > 0
        ) {

            throw new BadRequestException(
                    "Discount cannot exceed "
                            + maximumDiscountPercentage
                            + "%"
            );
        }


        /*
         * ============================================================
         * 3. VALIDATE ITEMS
         * ============================================================
         */

        if (
                request.getItems() == null ||
                        request.getItems().isEmpty()
        ) {

            throw new BadRequestException(
                    "Sale must contain at least one item"
            );
        }


        /*
         * ============================================================
         * 4. CREATE SALE
         * ============================================================
         */

        Sale sale =
                new Sale();


        sale.setCustomer(
                customer
        );


        sale.setDiscountPercentage(
                discountPercentage
        );


        sale.setNotes(
                normalize(
                        request.getNotes()
                )
        );


        /*
         * ============================================================
         * 5. GENERATE RECEIPT NUMBER
         *
         * IMPORTANT:
         * This MUST happen BEFORE saleRepository.save(sale)
         * because receipt_number is NOT NULL in the database.
         * ============================================================
         */

        sale.setReceiptNumber(
                receiptNumberService
                        .generateReceiptNumber()
        );


        /*
         * ============================================================
         * 6. PROCESS SALE ITEMS
         * ============================================================
         */

        BigDecimal subtotal =
                BigDecimal.ZERO;


        List<SaleItem> saleItems =
                new ArrayList<>();


        for (
                SaleItemRequest itemRequest
                : request.getItems()
        ) {

            /*
             * --------------------------------------------------------
             * Find product
             * --------------------------------------------------------
             */

            Product product =
                    productRepository.findById(
                            itemRequest.getProductId()
                    ).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Product not found with id: "
                                            + itemRequest.getProductId()
                            )
                    );


            /*
             * --------------------------------------------------------
             * Validate quantity
             * --------------------------------------------------------
             */

            BigDecimal quantity =
                    itemRequest.getQuantity();


            if (
                    quantity == null ||
                            quantity.compareTo(
                                    BigDecimal.ZERO
                            ) <= 0
            ) {

                throw new BadRequestException(
                        "Quantity must be greater than zero"
                );
            }


            /*
             * --------------------------------------------------------
             * Validate stock
             * --------------------------------------------------------
             */

            BigDecimal currentStock =
                    product.getStock();


            if (currentStock == null) {

                currentStock =
                        BigDecimal.ZERO;
            }


            if (
                    currentStock.compareTo(
                            quantity
                    ) < 0
            ) {

                throw new BadRequestException(
                        "Insufficient stock for product: "
                                + product.getProductName()
                );
            }


            /*
             * --------------------------------------------------------
             * Product selling price
             * --------------------------------------------------------
             */

            BigDecimal unitPrice =
                    product.getSellingPrice();


            if (unitPrice == null) {

                throw new BadRequestException(
                        "Product has no selling price: "
                                + product.getProductName()
                );
            }


            /*
             * --------------------------------------------------------
             * Calculate item subtotal
             * --------------------------------------------------------
             */

            BigDecimal itemSubtotal =
                    unitPrice
                            .multiply(quantity)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );


            subtotal =
                    subtotal.add(
                            itemSubtotal
                    );


            /*
             * --------------------------------------------------------
             * CREATE SALE ITEM
             * --------------------------------------------------------
             */

            SaleItem saleItem =
                    new SaleItem();


            saleItem.setSale(
                    sale
            );


            saleItem.setProduct(
                    product
            );


            saleItem.setQuantity(
                    quantity
            );


            saleItem.setUnitPrice(
                    unitPrice
            );


            saleItem.setSubtotal(
                    itemSubtotal
            );


            saleItems.add(
                    saleItem
            );
        }


        /*
         * ============================================================
         * 7. CALCULATE DISCOUNT
         * ============================================================
         */

        subtotal =
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                );


        BigDecimal discountAmount =
                subtotal
                        .multiply(
                                discountPercentage
                        )
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP
                        );


        /*
         * ============================================================
         * 8. CALCULATE GRAND TOTAL
         * ============================================================
         */

        BigDecimal total =
                subtotal
                        .subtract(
                                discountAmount
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        if (
                total.compareTo(
                        BigDecimal.ZERO
                ) < 0
        ) {

            throw new BadRequestException(
                    "Sale total cannot be negative"
            );
        }


        /*
         * ============================================================
         * 9. PAYMENT
         * ============================================================
         */

        BigDecimal paymentAmount =
                BigDecimal.ZERO;


        PaymentRequest paymentRequest =
                request.getPayment();


        if (paymentRequest != null) {

            if (
                    paymentRequest.getAmount() != null
            ) {

                paymentAmount =
                        paymentRequest.getAmount();
            }


            /*
             * --------------------------------------------------------
             * Payment cannot be negative
             * --------------------------------------------------------
             */

            if (
                    paymentAmount.compareTo(
                            BigDecimal.ZERO
                    ) < 0
            ) {

                throw new BadRequestException(
                        "Payment cannot be negative"
                );
            }


            /*
             * --------------------------------------------------------
             * Payment cannot exceed sale total
             * --------------------------------------------------------
             */

            if (
                    paymentAmount.compareTo(
                            total
                    ) > 0
            ) {

                throw new BadRequestException(
                        "Payment cannot exceed sale total"
                );
            }
        }


        /*
         * ============================================================
         * 10. CALCULATE BALANCE
         * ============================================================
         */

        BigDecimal balance =
                total
                        .subtract(
                                paymentAmount
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        /*
         * ============================================================
         * 11. SET SALE TOTALS
         * ============================================================
         */

        sale.setSubtotalAmount(
                subtotal
        );


        sale.setDiscountAmount(
                discountAmount
        );


        sale.setTotalAmount(
                total
        );


        sale.setPaidAmount(
                paymentAmount
        );


        sale.setBalance(
                balance
        );


        /*
         * ============================================================
         * 12. DETERMINE SALE STATUS
         * ============================================================
         */

        if (
                balance.compareTo(
                        BigDecimal.ZERO
                ) == 0
        ) {

            /*
             * Full payment
             */

            sale.setStatus(
                    SaleStatus.PAID
            );

        } else if (
                paymentAmount.compareTo(
                        BigDecimal.ZERO
                ) > 0
        ) {

            /*
             * Some payment but balance remains
             */

            sale.setStatus(
                    SaleStatus.PARTIAL
            );

        } else {

            /*
             * No payment
             */

            sale.setStatus(
                    SaleStatus.UNPAID
            );
        }


        /*
         * ============================================================
         * 13. ATTACH SALE ITEMS TO SALE
         * ============================================================
         */

        for (
                SaleItem saleItem
                : saleItems
        ) {

            saleItem.setSale(
                    sale
            );

            sale.getItems().add(
                    saleItem
            );
        }


        /*
         * ============================================================
         * 14. SAVE SALE
         *
         * Receipt number is already populated.
         * ============================================================
         */

        Sale savedSale =
                saleRepository.save(
                        sale
                );


        /*
         * ============================================================
         * 15. UPDATE STOCK + CREATE STOCK MOVEMENTS
         * ============================================================
         */

        for (
                SaleItem saleItem
                : saleItems
        ) {

            Product product =
                    saleItem.getProduct();


            BigDecimal previousStock =
                    product.getStock();


            if (previousStock == null) {

                previousStock =
                        BigDecimal.ZERO;
            }


            BigDecimal quantity =
                    saleItem.getQuantity();


            BigDecimal newStock =
                    previousStock
                            .subtract(
                                    quantity
                            );


            /*
             * --------------------------------------------------------
             * Safety check
             * --------------------------------------------------------
             */

            if (
                    newStock.compareTo(
                            BigDecimal.ZERO
                    ) < 0
            ) {

                throw new BadRequestException(
                        "Insufficient stock for product: "
                                + product.getProductName()
                );
            }


            /*
             * --------------------------------------------------------
             * Update product stock
             * --------------------------------------------------------
             */

            product.setStock(
                    newStock
            );


            productRepository.save(
                    product
            );


            /*
             * --------------------------------------------------------
             * Create stock movement
             * --------------------------------------------------------
             */

            StockMovement movement =
                    new StockMovement();


            movement.setProduct(
                    product
            );


            movement.setMovementType(
                    StockMovementType.SALE
            );


            movement.setQuantity(
                    quantity
            );


            movement.setPreviousStock(
                    previousStock
            );


            movement.setNewStock(
                    newStock
            );


            movement.setReason(
                    "Sale"
            );


            movement.setReference(
                    "SALE-" + savedSale.getId()
            );


            stockMovementRepository.save(
                    movement
            );
        }


        /*
         * ============================================================
         * 16. SAVE PAYMENT
         * ============================================================
         */

        if (
                paymentRequest != null &&
                        paymentAmount.compareTo(
                                BigDecimal.ZERO
                        ) > 0
        ) {

            Payment payment =
                    new Payment();


            payment.setSale(
                    savedSale
            );


            payment.setAmount(
                    paymentAmount
            );


            payment.setPaymentMethod(
                    paymentRequest.getPaymentMethod()
            );


            payment.setReference(
                    normalize(
                            paymentRequest.getReference()
                    )
            );


            payment.setNotes(
                    normalize(
                            paymentRequest.getNotes()
                    )
            );


            paymentRepository.save(
                    payment
            );
        }


        /*
         * ============================================================
         * 17. BUILD RECEIPT ITEMS
         * ============================================================
         */

        List<ReceiptItemResponse> receiptItems =
                savedSale
                        .getItems()
                        .stream()
                        .map(item -> {

                            Product product =
                                    item.getProduct();


                            return new ReceiptItemResponse(

                                    product.getId(),

                                    product.getProductName(),

                                    item.getQuantity(),

                                    item.getUnitPrice(),

                                    item.getSubtotal()

                            );

                        })
                        .toList();


        /*
         * ============================================================
         * 18. BUILD RECEIPT RESPONSE
         * ============================================================
         */

        ReceiptResponse receipt =
                new ReceiptResponse();


        receipt.setSaleId(
                savedSale.getId()
        );


        receipt.setReceiptNumber(
                savedSale.getReceiptNumber()
        );


        receipt.setSaleDate(
                savedSale.getSaleDate()
        );


        receipt.setCustomerId(
                customer.getId()
        );


        receipt.setCustomerName(
                customer.getFirstName()
                        + " "
                        + customer.getLastName()
        );


        receipt.setCustomerPhone(
                customer.getPhone()
        );


        receipt.setItems(
                receiptItems
        );


        receipt.setSubtotal(
                savedSale.getSubtotalAmount()
        );


        receipt.setDiscountPercentage(
                savedSale.getDiscountPercentage()
        );


        receipt.setDiscountAmount(
                savedSale.getDiscountAmount()
        );


        receipt.setTotal(
                savedSale.getTotalAmount()
        );


        receipt.setPaidAmount(
                savedSale.getPaidAmount()
        );


        receipt.setBalance(
                savedSale.getBalance()
        );


        receipt.setStatus(
                String.valueOf(
                        savedSale.getStatus()
                )
        );


        receipt.setNotes(
                savedSale.getNotes()
        );


        /*
         * ============================================================
         * 19. RETURN RECEIPT
         * ============================================================
         */

        return receipt;
    }





//
//    public ReceiptResponse createSale(
//            SaleRequest request
//    ){
//
//        /*
//         * --------------------------------------------------------
//         * 1. VALIDATE CUSTOMER
//         * --------------------------------------------------------
//         */
//
//        Customer customer =
//                customerRepository.findById(
//                        request.getCustomerId()
//                ).orElseThrow(
//                        () -> new ResourceNotFoundException(
//                                "Customer not found with id: "
//                                        + request.getCustomerId()
//                        )
//                );
//
//
//        /*
//         * Do not allow inactive customers
//         * to make new sales.
//         */
//
//        if (!customer.getActive()) {
//
//            throw new BadRequestException(
//                    "Customer is inactive"
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 2. VALIDATE DISCOUNT
//         * --------------------------------------------------------
//         */
//
//        BigDecimal discountPercentage =
//                request.getDiscountPercentage();
//
//        if (discountPercentage == null) {
//
//            discountPercentage =
//                    BigDecimal.ZERO;
//        }
//
//
//        if (discountPercentage.compareTo(
//                BigDecimal.ZERO
//        ) < 0) {
//
//            throw new BadRequestException(
//                    "Discount cannot be negative"
//            );
//        }
//
//
//        if (discountPercentage.compareTo(
//                maximumDiscountPercentage
//        ) > 0) {
//
//            throw new BadRequestException(
//                    "Discount cannot exceed "
//                            + maximumDiscountPercentage
//                            + "%"
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 3. VALIDATE ITEMS
//         * --------------------------------------------------------
//         */
//
//        if (
//                request.getItems() == null ||
//                        request.getItems().isEmpty()
//        ) {
//
//            throw new BadRequestException(
//                    "Sale must contain at least one item"
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 4. CREATE SALE
//         * --------------------------------------------------------
//         */
//
//        Sale sale =
//                new Sale();
//
//        sale.setCustomer(customer);
//
//        sale.setDiscountPercentage(
//                discountPercentage
//        );
//
//        sale.setNotes(
//                normalize(request.getNotes())
//        );
//
//
//        /*
//         * --------------------------------------------------------
//         * 5. PROCESS SALE ITEMS
//         * --------------------------------------------------------
//         */
//
//        BigDecimal subtotal =
//                BigDecimal.ZERO;
//
//
//        List<SaleItem> saleItems =
//                new ArrayList<>();
//
//
//        for (
//                SaleItemRequest itemRequest
//                : request.getItems()
//        ) {
//
//            /*
//             * Find product
//             */
//
//            Product product =
//                    productRepository.findById(
//                            itemRequest.getProductId()
//                    ).orElseThrow(
//                            () -> new ResourceNotFoundException(
//                                    "Product not found with id: "
//                                            + itemRequest.getProductId()
//                            )
//                    );
//
//
//            /*
//             * Validate quantity
//             */
//
//            BigDecimal quantity =
//                    itemRequest.getQuantity();
//
//
//            if (
//                    quantity == null ||
//                            quantity.compareTo(
//                                    BigDecimal.ZERO
//                            ) <= 0
//            ) {
//
//                throw new BadRequestException(
//                        "Quantity must be greater than zero"
//                );
//            }
//
//
//            /*
//             * Validate stock
//             */
//
//            BigDecimal currentStock =
//                    product.getStock();
//
//
//            if (currentStock == null) {
//
//                currentStock =
//                        BigDecimal.ZERO;
//            }
//
//
//            if (
//                    currentStock.compareTo(
//                            quantity
//                    ) < 0
//            ) {
//
//                throw new BadRequestException(
//                        "Insufficient stock for product: "
//                                + product.getProductName()
//                );
//            }
//
//
//            /*
//             * Product selling price
//             */
//
//            BigDecimal unitPrice =
//                    product.getSellingPrice();
//
//
//            if (unitPrice == null) {
//
//                throw new BadRequestException(
//                        "Product has no selling price: "
//                                + product.getProductName()
//                );
//            }
//
//
//            /*
//             * Calculate item subtotal
//             */
//
//            BigDecimal itemSubtotal =
//                    unitPrice.multiply(quantity)
//                            .setScale(
//                                    2,
//                                    RoundingMode.HALF_UP
//                            );
//
//
//            subtotal =
//                    subtotal.add(
//                            itemSubtotal
//                    );
//
//
//            /*
//             * ----------------------------------------------------
//             * CREATE SALE ITEM
//             * ----------------------------------------------------
//             */
//
//            SaleItem saleItem =
//                    new SaleItem();
//
//            saleItem.setSale(sale);
//
//            saleItem.setProduct(product);
//
//            saleItem.setQuantity(quantity);
//
//            saleItem.setUnitPrice(unitPrice);
//
//            saleItem.setSubtotal(itemSubtotal);
//
//
//            saleItems.add(
//                    saleItem
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 6. CALCULATE DISCOUNT
//         * --------------------------------------------------------
//         */
//
//        BigDecimal discountAmount =
//                subtotal
//                        .multiply(
//                                discountPercentage
//                        )
//                        .divide(
//                                BigDecimal.valueOf(100),
//                                2,
//                                RoundingMode.HALF_UP
//                        );
//
//
//        /*
//         * --------------------------------------------------------
//         * 7. CALCULATE GRAND TOTAL
//         * --------------------------------------------------------
//         */
//
//        BigDecimal total =
//                subtotal
//                        .subtract(
//                                discountAmount
//                        )
//                        .setScale(
//                                2,
//                                RoundingMode.HALF_UP
//                        );
//
//
//        if (
//                total.compareTo(
//                        BigDecimal.ZERO
//                ) < 0
//        ) {
//
//            throw new BadRequestException(
//                    "Sale total cannot be negative"
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 8. PAYMENT
//         * --------------------------------------------------------
//         */
//
//        BigDecimal paymentAmount =
//                BigDecimal.ZERO;
//
//
//        PaymentRequest paymentRequest =
//                request.getPayment();
//
//
//        if (paymentRequest != null) {
//
//            if (
//                    paymentRequest.getAmount() != null
//            ) {
//
//                paymentAmount =
//                        paymentRequest.getAmount();
//            }
//
//
//            if (
//                    paymentAmount.compareTo(
//                            BigDecimal.ZERO
//                    ) < 0
//            ) {
//
//                throw new BadRequestException(
//                        "Payment cannot be negative"
//                );
//            }
//
//
//            /*
//             * Payment cannot exceed sale total.
//             */
//
//            if (
//                    paymentAmount.compareTo(
//                            total
//                    ) > 0
//            ) {
//
//                throw new BadRequestException(
//                        "Payment cannot exceed sale total"
//                );
//            }
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 9. CALCULATE BALANCE
//         * --------------------------------------------------------
//         */
//
//        BigDecimal balance =
//                total
//                        .subtract(
//                                paymentAmount
//                        )
//                        .setScale(
//                                2,
//                                RoundingMode.HALF_UP
//                        );
//
//
//        /*
//         * --------------------------------------------------------
//         * 10. SET SALE TOTALS
//         * --------------------------------------------------------
//         */
//
//        sale.setSubtotalAmount(subtotal);
//
//        sale.setDiscountAmount(
//                discountAmount
//        );
//
//        sale.setTotalAmount(total);
//
//        sale.setPaidAmount(
//                paymentAmount
//        );
//
//        sale.setBalance(
//                balance
//        );
//
//
//        /*
//         * --------------------------------------------------------
//         * 11. DETERMINE SALE STATUS
//         * --------------------------------------------------------
//         */
//
//        if (
//                balance.compareTo(
//                        BigDecimal.ZERO
//                ) == 0
//        ) {
//
//            sale.setStatus(
//                    SaleStatus.PAID
//            );
//
//        } else if (
//                paymentAmount.compareTo(
//                        BigDecimal.ZERO
//                ) > 0
//        ) {
//
//            sale.setStatus(
//                    SaleStatus.PARTIAL
//            );
//
//        } else {
//
//            sale.setStatus(
//                    SaleStatus.UNPAID
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 12. SAVE SALE
//         * --------------------------------------------------------
//         */
//
//        Sale savedSale =
//                saleRepository.save(
//                        sale
//                );
//
//        sale.setReceiptNumber(
//                receiptNumberService.generateReceiptNumber()
//        );
//
//        savedSale =
//                saleRepository.save(savedSale);
//        /*
//         * --------------------------------------------------------
//         * 13. SAVE SALE ITEMS + UPDATE STOCK
//         * --------------------------------------------------------
//         */
//
//        for (
//                SaleItem saleItem
//                : saleItems
//        ) {
//
//            Product product =
//                    saleItem.getProduct();
//
//
//            BigDecimal previousStock =
//                    product.getStock();
//
//
//            BigDecimal quantity =
//                    saleItem.getQuantity();
//
//
//            BigDecimal newStock =
//                    previousStock.subtract(
//                            quantity
//                    );
//
//
//            /*
//             * Update product stock
//             */
//
//            product.setStock(
//                    newStock
//            );
//
//
//            productRepository.save(
//                    product
//            );
//
//
//            /*
//             * Link item to saved sale
//             */
//
//            saleItem.setSale(
//                    savedSale
//            );
//
//
//            /*
//             * Save sale item
//             */
//
//            // If Sale has cascade = CascadeType.ALL,
//            // this explicit save can be omitted.
//            // We keep it explicit for now.
//
//            savedSale
//                    .getItems()
//                    .add(saleItem);
//
//
//            /*
//             * ----------------------------------------------------
//             * STOCK MOVEMENT
//             * ----------------------------------------------------
//             */
//
//            StockMovement movement =
//                    new StockMovement();
//
//            movement.setProduct(
//                    product
//            );
//
//            movement.setMovementType(
//                    StockMovementType.SALE
//            );
//
//            movement.setQuantity(
//                    quantity
//            );
//
//            movement.setPreviousStock(
//                    previousStock
//            );
//
//            movement.setNewStock(
//                    newStock
//            );
//
//            movement.setReason(
//                    "Sale"
//            );
//
//            movement.setReference(
//                    "SALE-" + savedSale.getId()
//            );
//
//
//            stockMovementRepository.save(
//                    movement
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 14. SAVE PAYMENT
//         * --------------------------------------------------------
//         */
//
//        if (
//                paymentRequest != null &&
//                        paymentAmount.compareTo(
//                                BigDecimal.ZERO
//                        ) > 0
//        ) {
//
//            Payment payment =
//                    new Payment();
//
//            payment.setSale(
//                    savedSale
//            );
//
//            payment.setAmount(
//                    paymentAmount
//            );
//
//            payment.setPaymentMethod(
//                    paymentRequest.getPaymentMethod()
//            );
//
//            payment.setReference(
//                    normalize(
//                            paymentRequest.getReference()
//                    )
//            );
//
//            payment.setNotes(
//                    normalize(
//                            paymentRequest.getNotes()
//                    )
//            );
//
//
//            paymentRepository.save(
//                    payment
//            );
//        }
//
//
//        /*
//         * --------------------------------------------------------
//         * 15. BUILD RECEIPT RESPONSE
//         * --------------------------------------------------------
//         */
//
//        List<ReceiptItemResponse> receiptItems =
//                savedSale.getItems()
//                        .stream()
//                        .map(item -> {
//
//                            Product product =
//                                    item.getProduct();
//
//                            return new ReceiptItemResponse(
//
//                            );
//
//                        })
//                        .toList();
//
//
//        ReceiptResponse receipt =
//                new ReceiptResponse();
//
//
//        receipt.setSaleId(
//                savedSale.getId()
//        );
//
//        receipt.setReceiptNumber(
//                savedSale.getReceiptNumber()
//        );
//
//        receipt.setSaleDate(
//                savedSale.getSaleDate()
//        );
//
//        receipt.setCustomerId(
//                customer.getId()
//        );
//
//        receipt.setCustomerName(
//                customer.getFirstName()
//                        + " "
//                        + customer.getLastName()
//        );
//
//        receipt.setCustomerPhone(
//                customer.getPhone()
//        );
//
//        receipt.setItems(
//                receiptItems
//        );
//
//        receipt.setSubtotal(
//                savedSale.getSubtotalAmount()
//        );
//
//        receipt.setDiscountPercentage(
//                savedSale.getDiscountPercentage()
//        );
//
//        receipt.setDiscountAmount(
//                savedSale.getDiscountAmount()
//        );
//
//        receipt.setTotal(
//                savedSale.getTotalAmount()
//        );
//
//        receipt.setPaidAmount(
//                savedSale.getPaidAmount()
//        );
//
//        receipt.setBalance(
//                savedSale.getBalance()
//        );
//
//        receipt.setStatus(
//                String.valueOf(savedSale.getStatus())
//        );
//
//        receipt.setNotes(
//                savedSale.getNotes()
//        );
//
//
//        /*
//         * --------------------------------------------------------
//         * RETURN RECEIPT
//         * --------------------------------------------------------
//         */
//
//        return receipt;
//    }

//    private ReceiptResponse buildReceipt(
//            Sale sale
//    ) {
//
//        ReceiptResponse receipt =
//                new ReceiptResponse();
//
//
//        receipt.setReceiptNumber(
//                sale.getReceiptNumber()
//        );
//
//        receipt.setSaleDate(
//                sale.getSaleDate()
//        );
//
//
//        Customer customer =
//                sale.getCustomer();
//
//
//        if (customer != null) {
//
//            receipt.setCustomerId(
//                    customer.getId()
//            );
//
//            receipt.setCustomerName(
//                    customer.getFirstName()
//                            + " "
//                            + customer.getLastName()
//            );
//
//            receipt.setCustomerPhone(
//                    customer.getPhone()
//            );
//        }
//
//
//        List<SaleItemResponse> items =
//                sale.getItems()
//                        .stream()
//                        .map(item -> {
//
//                            Product product =
//                                    item.getProduct();
//
//                            return new SaleItemResponse(
//
//                                    product.getId(),
//
//                                    product.getProductName(),
//
//                                    item.getQuantity(),
//
//                                    item.getUnitPrice(),
//
//                                    item.getSubtotal()
//                            );
//
//                        })
//                        .toList();
//
//
//        receipt.setItems(items);
//
//
//        receipt.setSubtotal(
//                sale.getSubtotalAmount()
//        );
//
//        receipt.setDiscountPercentage(
//                sale.getDiscountPercentage()
//        );
//
//        receipt.setDiscountAmount(
//                sale.getDiscountAmount()
//        );
//
//        receipt.setTotal(
//                sale.getTotalAmount()
//        );
//
//        receipt.setPaidAmount(
//                sale.getPaidAmount()
//        );
//
//        receipt.setBalance(
//                sale.getBalance()
//        );
//
//        receipt.setStatus(
//                sale.getStatus()
//        );
//
//        receipt.setNotes(
//                sale.getNotes()
//        );
//
//
//        return receipt;
//    }

//    private String generateReceiptNumber(Long saleId) {
//
//        return String.format(
//                "REC-%s-%06d",
//                LocalDate.now().format(
//                        DateTimeFormatter.BASIC_ISO_DATE
//                ),
//                saleId
//        );
//    }



    @Transactional
    public SaleResponse getSaleById(Long id) {

        Sale sale =
                saleRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sale not found with id: " + id
                                )
                        );

        return mapToSaleResponse(sale);
    }

    @Transactional
    public Page<SaleResponse> getAllSales(
            Pageable pageable
    ) {

        return saleRepository
                .findAll(pageable)
                .map(this::mapToSaleResponse);
    }

    private SaleResponse mapToSaleResponse(
            Sale sale
    ) {

        SaleResponse response =
                new SaleResponse();


        response.setId(
                sale.getId()
        );

        response.setReceiptNumber(
                sale.getReceiptNumber()
        );

        response.setSaleDate(
                sale.getSaleDate()
        );


        Customer customer =
                sale.getCustomer();


        if (customer != null) {

            response.setCustomerId(
                    customer.getId()
            );

            response.setCustomerName(
                    customer.getFirstName()
                            + " "
                            + customer.getLastName()
            );

            response.setCustomerPhone(
                    customer.getPhone()
            );
        }


        response.setItems(
                sale.getItems()
                        .stream()
                        .map(item ->
                                new SaleItemResponse(

                                        item.getProduct().getId(),

                                        item.getProduct().getProductName(),

                                        item.getQuantity(),

                                        item.getUnitPrice(),

                                        item.getSubtotal()

                                )
                        )
                        .toList()
        );


        response.setSubtotal(
                sale.getSubtotalAmount()
        );

        response.setDiscountPercentage(
                sale.getDiscountPercentage()
        );

        response.setDiscountAmount(
                sale.getDiscountAmount()
        );

        response.setTotal(
                sale.getTotalAmount()
        );

        response.setPaidAmount(
                sale.getPaidAmount()
        );

        response.setBalance(
                sale.getBalance()
        );

        response.setStatus(
                sale.getStatus()
        );

        response.setNotes(
                sale.getNotes()
        );


        return response;
    }

    /*
     * ============================================================
     * PAYMENT SERVICE
     * ============================================================
     */

    @Transactional
    public SalePaymentResponse makePayment(
            Long saleId,
            SalePaymentRequest request
    ) {

        /*
         * ============================================================
         * 1. FIND SALE
         * ============================================================
         */

        Sale sale =
                saleRepository.findById(
                        saleId
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Sale not found with id: "
                                        + saleId
                        )
                );


        /*
         * ============================================================
         * 2. CHECK SALE STATUS
         * ============================================================
         */

        if (
                sale.getStatus() == SaleStatus.PAID
        ) {

            throw new BadRequestException(
                    "This sale is already fully paid"
            );
        }


        if (
                sale.getStatus() == SaleStatus.CANCELLED
        ) {

            throw new BadRequestException(
                    "Cancelled sales cannot receive payments"
            );
        }


        /*
         * ============================================================
         * 3. VALIDATE PAYMENT AMOUNT
         * ============================================================
         */

        BigDecimal paymentAmount =
                request.getAmount();


        if (
                paymentAmount == null ||
                        paymentAmount.compareTo(
                                BigDecimal.ZERO
                        ) <= 0
        ) {

            throw new BadRequestException(
                    "Payment amount must be greater than zero"
            );
        }


        /*
         * ============================================================
         * 4. GET CURRENT PAYMENTS
         * ============================================================
         */

        BigDecimal previousPaid =
                paymentRepository
                        .getTotalPaidBySaleId(
                                saleId
                        );


        if (previousPaid == null) {

            previousPaid =
                    BigDecimal.ZERO;
        }


        /*
         * ============================================================
         * 5. CALCULATE REMAINING BALANCE
         * ============================================================
         */

        BigDecimal saleTotal =
                sale.getTotalAmount();


        BigDecimal currentBalance =
                saleTotal
                        .subtract(
                                previousPaid
                        );


        /*
         * ============================================================
         * 6. PAYMENT CANNOT EXCEED BALANCE
         * ============================================================
         */

        if (
                paymentAmount.compareTo(
                        currentBalance
                ) > 0
        ) {

            throw new BadRequestException(
                    "Payment cannot exceed remaining balance. "
                            + "Remaining balance: "
                            + currentBalance
            );
        }


        /*
         * ============================================================
         * 7. SAVE PAYMENT
         * ============================================================
         */

        Payment payment =
                new Payment();


        payment.setSale(
                sale
        );


        payment.setAmount(
                paymentAmount
        );


        payment.setPaymentMethod(
                request.getPaymentMethod()
        );


        payment.setReference(
                normalize(
                        request.getReference()
                )
        );


        payment.setNotes(
                normalize(
                        request.getNotes()
                )
        );


        paymentRepository.save(
                payment
        );


        /*
         * ============================================================
         * 8. CALCULATE NEW TOTAL PAID
         * ============================================================
         */

        BigDecimal totalPaid =
                previousPaid
                        .add(
                                paymentAmount
                        );


        BigDecimal newBalance =
                saleTotal
                        .subtract(
                                totalPaid
                        );


        /*
         * ============================================================
         * 9. UPDATE SALE
         * ============================================================
         */

        sale.setPaidAmount(
                totalPaid
        );


        sale.setBalance(
                newBalance
        );


        /*
         * ============================================================
         * 10. DETERMINE NEW STATUS
         * ============================================================
         */

        if (
                newBalance.compareTo(
                        BigDecimal.ZERO
                ) == 0
        ) {

            sale.setStatus(
                    SaleStatus.PAID
            );

        } else {

            sale.setStatus(
                    SaleStatus.PARTIAL
            );
        }


        saleRepository.save(
                sale
        );


        /*
         * ============================================================
         * 11. RESPONSE
         * ============================================================
         */

        SalePaymentResponse response =
                new SalePaymentResponse();


        response.setSaleId(
                sale.getId()
        );


        response.setReceiptNumber(
                sale.getReceiptNumber()
        );


        response.setSaleTotal(
                saleTotal
        );


        response.setPreviousPaidAmount(
                previousPaid
        );


        response.setPaymentAmount(
                paymentAmount
        );


        response.setTotalPaidAmount(
                totalPaid
        );


        response.setBalance(
                newBalance
        );


        response.setStatus(
                sale.getStatus()
        );


        return response;
    }

    @Transactional
    public List<SaleResponse> getOutstandingSales() {

        List<Sale> sales =
                saleRepository
                        .findByBalanceGreaterThanOrderBySaleDateDesc(
                                BigDecimal.ZERO
                        );

        return sales.stream()
                .map(this::mapToSaleResponse)
                .toList();
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