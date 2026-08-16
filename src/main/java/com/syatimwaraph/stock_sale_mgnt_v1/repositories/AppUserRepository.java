package com.syatimwaraph.stock_sale_mgnt_v1.repositories;


import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}