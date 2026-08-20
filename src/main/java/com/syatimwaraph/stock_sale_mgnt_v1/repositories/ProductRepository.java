package com.syatimwaraph.stock_sale_mgnt_v1.repositories;

import com.syatimwaraph.stock_sale_mgnt_v1.entity.Product;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByProductCategory(
            ProductCategory category
    );

    List<Product> findByProductNameContainingIgnoreCase(
            String productName
    );

    boolean existsByProductNameIgnoreCase(
            String productName
    );

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.stock > 0
      AND p.stock < p.alertStock
    ORDER BY p.stock ASC
""")
    List<Product> findLowStockProducts();

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.stock <= 0
    ORDER BY p.productName ASC
""")
    List<Product> findOutOfStockProducts();
}