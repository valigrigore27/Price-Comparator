package org.example.market.service;

import org.example.market.dto.PriceHistoryDto;
import org.example.market.model.PriceEntry;
import org.example.market.repository.PriceEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceHistoryService {

    private final PriceEntryRepository priceEntryRepository;

    public PriceHistoryService(PriceEntryRepository priceEntryRepository) {
        this.priceEntryRepository = priceEntryRepository;
    }

    //retrieves the price history for a product, optionally filtered by category, brand, and store
    public List<PriceHistoryDto> getPriceHistory(String productName, Optional<String> productCategory, Optional<String> brand, Optional<String> storeName) {
        List<PriceEntry> entries = entriesByParameters(productName, productCategory, brand, storeName);

        // map each PriceEntry to a DTO for returning as API response
        return entries.stream()
                .map(e -> new PriceHistoryDto(e.getDate(), e.getPrice(), e.getCurrency(), e.getStore().getStoreName()))
                .toList();
    }

    //builds and executes the appropriate query based on which optional filters are present
    public List<PriceEntry> entriesByParameters(String productName, Optional<String> productCategory, Optional<String> brand, Optional<String> storeName){
        if(productCategory.isPresent()){
            if (brand.isPresent()){
                if(storeName.isPresent()){
                    return priceEntryRepository.findByProduct_ProductNameAndProduct_ProductCategoryAndProductBrandAndStore_StoreNameOrderByDateAsc(productName, productCategory.get(), brand.get(), storeName.get());
                }
                return priceEntryRepository.findByProduct_ProductNameAndProduct_ProductCategoryAndProductBrandOrderByDateAsc(productName, productCategory.get(), brand.get());
            }

            if(storeName.isPresent()) {
                return priceEntryRepository.findByProduct_ProductNameAndProduct_ProductCategoryAndStore_StoreNameOrderByDateAsc(productName, productCategory.get(), storeName.get());
            }
            return priceEntryRepository.findByProduct_ProductNameAndProduct_ProductCategoryOrderByDateAsc(productName, productCategory.get());
        }
        if(brand.isPresent()){
            if(storeName.isPresent()){
                return priceEntryRepository.findByProduct_ProductNameAndProductBrandAndStore_StoreNameOrderByDateAsc(productName, brand.get(), storeName.get());
            }
            return priceEntryRepository.findByProduct_ProductNameAndProductBrandOrderByDateAsc(productName, brand.get());
        }
        if(storeName.isPresent()){
            return priceEntryRepository.findByProduct_ProductNameAndStore_StoreNameOrderByDateAsc(productName, storeName.get());
        }
        return priceEntryRepository.findByProduct_ProductNameOrderByDateAsc(productName);
    }

}
