package com.syatimwaraph.stock_sale_mgnt_v1.repositories;


import com.syatimwaraph.stock_sale_mgnt_v1.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    /*
     * ============================================================
     * PAYMENTS FOR A SALE
     * ============================================================
     */

    List<Payment> findBySaleIdOrderByPaymentDateAsc(
            Long saleId
    );


    /*
     * ============================================================
     * LATEST PAYMENTS
     * ============================================================
     */

    List<Payment> findTop20ByOrderByPaymentDateDesc();
}