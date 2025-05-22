package org.example.market.service;

import org.example.market.model.Discount;
import org.example.market.model.PriceEntry;
import org.example.market.model.Product;
import org.example.market.model.Store;
import org.example.market.repository.DiscountRepository;
import org.example.market.repository.PriceEntryRepository;
import org.example.market.repository.ProductRepository;
import org.example.market.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CustomPriceAlertService {
    private final ProductRepository productRepository;
    private final PriceEntryRepository priceEntryRepository;
    private final StoreRepository storeRepository;
    private final DiscountRepository discountRepository;

    public CustomPriceAlertService(ProductRepository productRepository, PriceEntryRepository priceEntryRepository, StoreRepository storeRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.priceEntryRepository = priceEntryRepository;
        this.storeRepository = storeRepository;
        this.discountRepository = discountRepository;
    }

    public void priceOfProductDroppedBelow(String productName, String storeName, double desiredPrice){

        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            System.out.println("Store not found: " + storeName);
            return;
        }

        for(Product product : products){
            List<PriceEntry> priceEntries = priceEntryRepository.findByProductAndStore(product, store);
            List<Discount> discounts = discountRepository.findByProductAndStore(product, store);

            if (!priceEntries.isEmpty()) {
                priceEntries.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                double lastProductPriceInStore = priceEntries.get(0).getPrice();
                //System.out.println(lastProductPriceInStore);

                if (!discounts.isEmpty()) {
                    discounts.sort((a, b) -> b.getDate().compareTo(a.getDate()));

                    LocalDate fromDate = LocalDate.parse(discounts.get(0).getFromDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate toDate = LocalDate.parse(discounts.get(0).getToDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    double percentageOfDiscount = discounts.get(0).getPercentageOfDiscount();
                    //System.out.println(percentageOfDiscount);

                    LocalDate today = LocalDate.now();
                    if(!fromDate.isAfter(today) && !toDate.isBefore(today)) {
                        lastProductPriceInStore *= (100 - percentageOfDiscount) / 100.0;
                    }
                }
                if(lastProductPriceInStore <= desiredPrice) {
                    System.out.printf("The price for product %s from %s store dropped below %.2f ! Now it's %s.", productName, storeName, desiredPrice, lastProductPriceInStore);
                    System.out.println("\n-------------------------------\n");
                }
            }
            }
            }
        }

