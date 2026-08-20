package com.syatimwaraph.stock_sale_mgnt_v1.repositories;


import com.syatimwaraph.stock_sale_mgnt_v1.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemRepository
        extends JpaRepository<SaleItem, Long> {

    /*
     * ============================================================
     * ITEMS FOR A SALE
     * ============================================================
     */

    List<SaleItem> findBySaleId(Long saleId);


    /*
     * ============================================================
     * ITEMS SOLD FOR A PRODUCT
     * ============================================================
     */

    List<SaleItem> findByProductId(Long productId);


    /*
     * ============================================================
     * ITEMS FOR A SALE ORDERED
     * ============================================================
     */

    List<SaleItem> findBySaleIdOrderByIdAsc(
            Long saleId
    );
}