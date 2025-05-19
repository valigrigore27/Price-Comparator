package org.example.market.repository;
import org.example.market.model.Product;
import org.example.market.model.Store;
import org.example.market.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    boolean existsByProductAndStoreAndFromDateAndToDate(Product product, Store store, String from, String to);
}
