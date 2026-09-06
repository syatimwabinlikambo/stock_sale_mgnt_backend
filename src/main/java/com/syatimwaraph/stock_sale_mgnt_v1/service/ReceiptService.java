package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.ReceiptItemResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.ReceiptResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Payment;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Sale;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReceiptService {

    private final SaleRepository saleRepository;

    public ReceiptService(
            SaleRepository saleRepository
    ) {
        this.saleRepository = saleRepository;
    }


    public ReceiptResponse generateReceipt(Long saleId) {

        Sale sale =
                saleRepository.findById(saleId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sale not found with id: " + saleId
                                )
                        );


        ReceiptResponse response =
                new ReceiptResponse();


        response.setSaleId(
                sale.getId()
        );

        response.setReceiptNumber(
                sale.getReceiptNumber()
        );

        response.setSaleDate(
                sale.getSaleDate()
        );


        /*
         * CUSTOMER
         */

        if (sale.getCustomer() != null) {

            response.setCustomerId(
                    sale.getCustomer().getId()
            );

            response.setCustomerName(
                    sale.getCustomer().getFirstName()
                            + " "
                            + sale.getCustomer().getLastName()
            );

            response.setCustomerPhone(
                    sale.getCustomer().getPhone()
            );
        }


        /*
         * ITEMS
         */

        List<ReceiptItemResponse> items =
                sale.getItems()
                        .stream()
                        .map(item -> {

                            ReceiptItemResponse dto =
                                    new ReceiptItemResponse();


                            dto.setProductId(
                                    item.getProduct().getId()
                            );

                            dto.setProductName(
                                    item.getProduct().getProductName()
                            );

                            dto.setQuantity(
                                    item.getQuantity()
                            );

                            dto.setUnitPrice(
                                    item.getUnitPrice()
                            );

                            dto.setSubtotal(
                                    item.getSubtotal()
                            );


                            return dto;

                        })
                        .toList();


        response.setItems(items);


        /*
         * AMOUNTS
         */

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


        /*
         * STATUS
         */

        response.setStatus(
                sale.getStatus().name()
        );


        /*
         * NOTES
         */

        response.setNotes(
                sale.getNotes()
        );


        /*
         * PAYMENT
         */

        if (sale.getPayments() != null
                && !sale.getPayments().isEmpty()) {

            Payment payment =
                    sale.getPayments()
                            .get(
                                    sale.getPayments().size() - 1
                            );


            response.setPaymentMethod(
                    payment.getPaymentMethod().name()
            );

            response.setPaymentReference(
                    payment.getReference()
            );
        }


        return response;
    }
}