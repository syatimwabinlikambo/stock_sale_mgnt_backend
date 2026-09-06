package com.syatimwaraph.stock_sale_mgnt_v1.repositories;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.SaleResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Sale;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.SaleStatus;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    boolean existsByReceiptNumber(String receiptNumber);

    List<Sale> findByBalanceGreaterThanOrderBySaleDateDesc(
            BigDecimal balance
    );

    /*
     * ============================================================
     * RECENT SALES
     * ============================================================
     */

    List<Sale> findTop20ByOrderBySaleDateDesc();

    Page<Sale> findAllByOrderBySaleDateDesc(
            Pageable pageable
    );

    Optional<Sale> findByReceiptNumber(
            String receiptNumber
    );

//    @Transactional(readOnly = true)
//    public SaleResponse getSaleById(Long id) {
//
//        SaleRepository saleRepository = null;
//        Sale sale =
//                saleRepository.findById(id)
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Sale not found with id: " + id
//                                )
//                        );
//
//        return mapToResponse(sale);
//    }

}