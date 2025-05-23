package org.example.market.repository;

import org.example.market.model.PriceEntry;
import org.example.market.model.Product;
import org.example.market.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {
    boolean existsByProductAndStoreAndDate(Product product, Store store, String date);

    boolean existsByProduct(Product product);


    List<PriceEntry> findByProduct(Product product);

    List<PriceEntry> findByProductAndStore(Product product, Store store);

    //find by product name
    List<PriceEntry> findByProduct_ProductNameOrderByDateAsc(String productName);

    //find by product name and store name
    List<PriceEntry> findByProduct_ProductNameAndStore_StoreNameOrderByDateAsc(String productName, String s);

    //find by product name and brand
    List<PriceEntry> findByProduct_ProductNameAndProductBrandOrderByDateAsc(String productName, String s);

    //find by product name and product category
    List<PriceEntry> findByProduct_ProductNameAndProduct_ProductCategoryOrderByDateAsc(String productName, String s);

    //find by product name and brand and store name
    List<PriceEntry> findByProduct_ProductNameAndProductBrandAndStore_StoreNameOrderByDateAsc(String productName, String s, String s1);

    //find by product name and product category and brand
    List<PriceEntry> findByProduct_ProductNameAndProduct_ProductCategoryAndProductBrandOrderByDateAsc(String productName, String s, String s1);

    //find by product name and product category and store name
    List<PriceEntry> findByProduct_ProductNameAndProduct_ProductCategoryAndStore_StoreNameOrderByDateAsc(String productName, String s, String s1);

    //product name and product category and brand and store name
    List<PriceEntry> findByProduct_ProductNameAndProduct_ProductCategoryAndProductBrandAndStore_StoreNameOrderByDateAsc(String productName, String s, String s1, String s2);
}
