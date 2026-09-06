package com.syatimwaraph.stock_sale_mgnt_v1.repositories;


import com.syatimwaraph.stock_sale_mgnt_v1.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    List<Payment> findBySaleIdOrderByPaymentDateDesc(
            Long saleId
    );


    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.sale.id = :saleId
    """)
    BigDecimal getTotalPaidBySaleId(
            @Param("saleId") Long saleId
    );
}