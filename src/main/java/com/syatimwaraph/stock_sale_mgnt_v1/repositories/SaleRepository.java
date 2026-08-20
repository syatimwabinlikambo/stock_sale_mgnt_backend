package com.syatimwaraph.stock_sale_mgnt_v1.repositories;

import com.syatimwaraph.stock_sale_mgnt_v1.entity.Sale;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface SaleRepository
        extends JpaRepository<Sale, Long> {

    /*
     * ============================================================
     * CUSTOMER SALES
     * ============================================================
     */

    Page<Sale> findByCustomerId(
            Long customerId,
            Pageable pageable
    );


    /*
     * ============================================================
     * SALES BY STATUS
     * ============================================================
     */

    Page<Sale> findByStatus(
            SaleStatus status,
            Pageable pageable
    );


    /*
     * ============================================================
     * CUSTOMER + STATUS
     * ============================================================
     */

    Page<Sale> findByCustomerIdAndStatus(
            Long customerId,
            SaleStatus status,
            Pageable pageable
    );

    Page<Sale> findByBalanceGreaterThan(
            BigDecimal balance,
            Pageable pageable
    );

    /*
     * ============================================================
     * RECENT SALES
     * ============================================================
     */

    List<Sale> findTop20ByOrderBySaleDateDesc();


}