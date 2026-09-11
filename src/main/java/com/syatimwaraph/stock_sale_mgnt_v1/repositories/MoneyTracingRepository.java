package com.syatimwaraph.stock_sale_mgnt_v1.repositories;

import com.syatimwaraph.stock_sale_mgnt_v1.entity.MoneyTracing;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyTracingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyTracingRepository
        extends JpaRepository<MoneyTracing, Long> {

    List<MoneyTracing>
    findAllByOrderBySubmittedAtDesc();


    List<MoneyTracing>
    findByStatusOrderBySubmittedAtDesc(
            MoneyTracingStatus status
    );


    List<MoneyTracing>
    findBySubmittedByIdOrderBySubmittedAtDesc(
            Long userId
    );
}