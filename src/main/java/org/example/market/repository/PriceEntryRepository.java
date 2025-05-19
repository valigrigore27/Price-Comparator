package org.example.market.repository;

import org.example.market.model.PriceEntry;
import org.example.market.model.Product;
import org.example.market.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {
    boolean existsByProductAndStoreAndDate(Product product, Store store, String date);

}
