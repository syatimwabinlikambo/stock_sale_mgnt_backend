package com.syatimwaraph.stock_sale_mgnt_v1.repositories;


import com.syatimwaraph.stock_sale_mgnt_v1.entity.StockMovement;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.StockMovementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductIdOrderByCreatedAtDesc(
            Long productId
    );

    List<StockMovement> findAllByOrderByCreatedAtDesc();

    List<StockMovement> findByMovementTypeOrderByCreatedAtDesc(
            StockMovementType movementType
    );
}