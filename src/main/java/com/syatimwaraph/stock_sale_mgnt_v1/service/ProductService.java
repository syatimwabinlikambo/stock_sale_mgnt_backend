package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.ProductRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.ProductResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Product;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(
            ProductRepository productRepository
    ) {

        this.productRepository =
                productRepository;
    }


    // ========================================================
    // GET ALL
    // ========================================================

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ========================================================
    // GET BY ID
    // ========================================================

    @Transactional(readOnly = true)
    public ProductResponse getProductById(
            Long id
    ) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(product);
    }


    // ========================================================
    // CREATE
    // ========================================================

    public ProductResponse createProduct(
            ProductRequest request
    ) {

        String name =
                request.getProductName()
                        .trim();


        if (
                productRepository
                        .existsByProductNameIgnoreCase(name)
        ) {

            throw new RuntimeException(
                    "A product with this name already exists"
            );
        }


        Product product =
                new Product();


        product.setProductName(name);

        product.setProductCategory(
                request.getProductCategory()
        );

        product.setPurchasePrice(
                request.getPurchasePrice()
        );

        product.setSellingPrice(
                request.getSellingPrice()
        );

        product.setStock(
                request.getStock()
        );

        product.setUnit(
                request.getUnit().trim()
        );

        product.setAlertStock(
                request.getAlertStock()
        );


        Product saved =
                productRepository.save(product);


        return mapToResponse(saved);
    }


    // ========================================================
    // UPDATE
    // ========================================================

    public ProductResponse updateProduct(
            Long id,
            ProductRequest request
    ) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with id: "
                                                + id
                                )
                        );


        String name =
                request.getProductName()
                        .trim();


        product.setProductName(name);

        product.setProductCategory(
                request.getProductCategory()
        );

        product.setPurchasePrice(
                request.getPurchasePrice()
        );

        product.setSellingPrice(
                request.getSellingPrice()
        );

        product.setStock(
                request.getStock()
        );

        product.setUnit(
                request.getUnit().trim()
        );

        product.setAlertStock(
                request.getAlertStock()
        );


        Product updated =
                productRepository.save(product);


        return mapToResponse(updated);
    }


    // ========================================================
    // DELETE
    // ========================================================

    public void deleteProduct(
            Long id
    ) {

        if (!productRepository.existsById(id)) {

            throw new RuntimeException(
                    "Product not found with id: "
                            + id
            );
        }


        productRepository.deleteById(id);
    }


    // ========================================================
    // MAPPER
    // ========================================================

    private ProductResponse mapToResponse(
            Product product
    ) {

        return new ProductResponse(

                product.getId(),

                product.getProductName(),

                product.getProductCategory(),

                product.getPurchasePrice(),

                product.getSellingPrice(),

                product.getStock(),

                product.getUnit(),

                product.getAlertStock(),

                product.getCreatedAt(),

                product.getUpdatedAt()
        );
    }
}