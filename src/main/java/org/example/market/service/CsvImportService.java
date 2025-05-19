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
        Resource[] resources = resolver.getResources("classpath:data/*.csv");

        for (Resource resource : resources) {
            String filename = Objects.requireNonNull(resource.getFilename());
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
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if(fields.length < 8) continue;

                Product product = getOrCreateProduct(fields, resource);

                double price = Double.parseDouble(fields[6]);
                String currency = fields[7];

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
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if(fields.length < 9) continue;

                Product product = getOrCreateProduct(fields, resource);

                String fromDate = fields[6];
                String toDate = fields[7];
                int percentage = Integer.parseInt(fields[8]);

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

    private Product getOrCreateProduct(String[] fields, Resource resource) {
        String filename = Objects.requireNonNull(resource.getFilename());
        String productId = fields[0];
        return productRepository.findById(productId).orElseGet(() -> {
            if (filename.contains("discounts")) {
                Product product = Product.builder()
                        .productId(fields[0])
                        .productName(fields[1])
                        .brand(fields[2])
                        .packageQuantity(Double.parseDouble(fields[3]))
                        .packageUnit(fields[4])
                        .productCategory(fields[5])
                        .build();
                return productRepository.save(product);
            } else {
                Product product = Product.builder()
                        .productId(fields[0])
                        .productName(fields[1])
                        .productCategory(fields[2])
                        .brand(fields[3])
                        .packageQuantity(Double.parseDouble(fields[4]))
                        .packageUnit(fields[5])
                        .build();
                return productRepository.save(product);
            }
        });
    }


    private Store getOrCreateStore(String name) {
        return storeRepository.findById(name)
                .orElseGet(() -> storeRepository.save(Store.builder().name(name).build()));
    }
}
