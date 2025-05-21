package org.example.market.repository;

import org.example.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Override
    Optional<Product> findById(String string);

    @Override
    boolean existsById(String string);

    List<Product> findByProductNameContainingIgnoreCase(String productName);

    Product findByProductNameAndBrand(String productName, String brand);
}