package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.example.market.model.*;
import org.example.market.repository.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceEntryRepository priceEntryRepository;
    private final DiscountRepository discountRepository;

    @Transactional
    public void importAll() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //loads all CSV files located in resources/data/
        Resource[] resources = resolver.getResources("classpath:data/*.csv");

        for (Resource resource : resources) {
            String filename = Objects.requireNonNull(resource.getFilename());
            // distinguish between price and discount files based on filename
            if (filename.contains("discounts")) {
                importDiscountFile(resource);
            } else {
                importPriceFile(resource);
            }
        }
    }

    private void importPriceFile(Resource resource) throws Exception {
        String filename = Objects.requireNonNull(resource.getFilename());
        String[] parts = filename.replace(".csv", "").split("_");
        String storeName = parts[0];
        String date = parts.length > 1 ? parts[1] : "";

        Store store = getOrCreateStore(storeName);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine(); //skip CSV header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length < 8) continue;

                Product product = getOrCreateProduct(fields);

                double price = Double.parseDouble(fields[6]);
                String currency = fields[7];

                // avoid duplicate entries by checking existing price record for same product/store/date
                if (!priceEntryRepository.existsByProductAndStoreAndDate(product, store, date)) {
                    PriceEntry entry = PriceEntry.builder()
                            .product(product)
                            .store(store)
                            .date(date)
                            .price(price)
                            .currency(currency)
                            .build();
                    priceEntryRepository.save(entry);
                }
            }
        }
    }

    private void importDiscountFile(Resource resource) throws Exception {
        String filename = Objects.requireNonNull(resource.getFilename());
        String[] parts = filename.replace(".csv", "").split("_");
        String storeName = parts[0];
        String date = parts.length > 1 ? parts[2] : "";

        Store store = getOrCreateStore(storeName);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length < 9) continue;

                //only apply discount if the product is already in db
                Product product = productRepository.findById(fields[0]).orElse(null);
                if (product == null) {
                    System.out.println("\n-------------------------------\n");
                    System.out.printf("Skipping discount for unknown product ID: %s%n", fields[0]);
                    System.out.println("\n-------------------------------\n");
                    continue;
                }

                // only add discounts to products that have at least one price history entry
                boolean hasPriceHistory = priceEntryRepository.existsByProduct(product);
                if (!hasPriceHistory) {
                    System.out.printf("Skipping discount for product without price history: %s%n", product.getProductName());
                    System.out.println("\n-------------------------------\n");
                    continue;
                }

                String fromDate = fields[6];
                String toDate = fields[7];
                int percentage = Integer.parseInt(fields[8]);

                //Avoid inserting duplicate discounts
                if (!discountRepository.existsByProductAndStoreAndFromDateAndToDate(product, store, fromDate, toDate)) {
                    Discount discount = Discount.builder()
                            .product(product)
                            .store(store)
                            .date(date)
                            .fromDate(fromDate)
                            .toDate(toDate)
                            .percentageOfDiscount(percentage)
                            .build();
                    discountRepository.save(discount);
                }
            }
        }
    }


      //takes an existing product by ID or creates it if it doesn't exist
    private Product getOrCreateProduct(String[] fields) {
        String productId = fields[0];
        return productRepository.findById(productId).orElseGet(() -> {
            Product product = Product.builder()
                    .productId(fields[0])
                    .productName(fields[1])
                    .productCategory(fields[2])
                    .brand(fields[3])
                    .packageQuantity(Double.parseDouble(fields[4]))
                    .packageUnit(fields[5])
                    .build();
            return productRepository.save(product);
        });
    }

    private Store getOrCreateStore(String name) {
        return storeRepository.findById(name)
                .orElseGet(() -> storeRepository.save(Store.builder().storeName(name).build()));
    }
}
