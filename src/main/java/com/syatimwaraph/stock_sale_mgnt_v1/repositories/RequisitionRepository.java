package com.syatimwaraph.stock_sale_mgnt_v1.repositories;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.RequisitionStatus;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisitionRepository
        extends JpaRepository<Requisition, Long> {

    List<Requisition>
    findByStatusOrderByRequestedAtDesc(
            RequisitionStatus status
    );

    List<Requisition>
    findByRequestedByIdOrderByRequestedAtDesc(
            Long userId
    );

    List<Requisition>
    findAllByOrderByRequestedAtDesc();
}