package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.StockMovementRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.Product;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.StockMovement;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.BadRequestException;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.ResourceNotFoundException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.ProductRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.StockMovementRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class StockService {

    private final ProductRepository productRepository;

    private final StockMovementRepository stockMovementRepository;


    public StockService(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository
    ) {
        this.productRepository =
                productRepository;

        this.stockMovementRepository =
                stockMovementRepository;
    }


    public StockMovement createMovement(
            StockMovementRequest request
    ) {

        Product product =
                productRepository.findById(
                        request.getProductId()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Product not found"
                        )
                );


        BigDecimal previousStock =
                product.getStock();


        BigDecimal newStock;


        switch (request.getMovementType()) {

            case STOCK_IN:

                newStock =
                        previousStock.add(
                                request.getQuantity()
                        );

                break;


            case STOCK_OUT:

            case SALE:

                newStock =
                        previousStock.subtract(
                                request.getQuantity()
                        );


                if (newStock.compareTo(
                        BigDecimal.ZERO
                ) < 0) {

                    throw new BadRequestException(
                            "Insufficient stock"
                    );

                }

                break;


            case ADJUSTMENT:

                newStock =
                        request.getQuantity();

                break;


            default:

                throw new BadRequestException(
                        "Unsupported stock movement type"
                );
        }


        product.setStock(newStock);

        productRepository.save(product);


        StockMovement movement =
                new StockMovement();


        movement.setProduct(product);

        movement.setMovementType(
                request.getMovementType()
        );

        movement.setQuantity(
                request.getQuantity()
        );

        movement.setPreviousStock(
                previousStock
        );

        movement.setNewStock(
                newStock
        );

        movement.setReason(
                request.getReason()
        );

        movement.setReference(
                request.getReference()
        );


        return stockMovementRepository.save(
                movement
        );
    }

    public List<StockMovement> getAllMovements() {

        return stockMovementRepository
                .findAllByOrderByCreatedAtDesc();
    }

    public List<StockMovement> getProductMovements(
            Long productId
    ) {

        if (!productRepository.existsById(productId)) {

            throw new ResourceNotFoundException(
                    "Product with ID "
                            + productId
                            + " not found"
            );
        }


        return stockMovementRepository
                .findByProductIdOrderByCreatedAtDesc(
                        productId
                );
    }

    public List<Product> getLowStockProducts() {

        return productRepository
                .findLowStockProducts();
    }

    public List<Product> getOutOfStockProducts() {

        return productRepository
                .findOutOfStockProducts();
    }
}