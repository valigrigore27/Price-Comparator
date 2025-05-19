package org.example.market.service;

import org.example.market.model.PriceEntry;
import org.example.market.model.Product;
import org.example.market.repository.PriceEntryRepository;
import org.example.market.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductSubstitutesService {
    private final PriceEntryRepository priceEntryRepository;

    public ProductSubstitutesService(PriceEntryRepository priceEntryRepository) {
        this.priceEntryRepository = priceEntryRepository;
    }

    @Transactional
    public void calculatePricePerUnit() {
        List<PriceEntry> entries = priceEntryRepository.findAll();

        for (PriceEntry entry : entries) {
            Product product = entry.getProduct();
            double quantity = product.getPackageQuantity();
            String unit = product.getPackageUnit().toLowerCase();

            double normalizedQuantity = normalize(quantity, unit);

            if (normalizedQuantity > 0) {
                double pricePerUnit = entry.getPrice() / normalizedQuantity;
                entry.setPricePerUnit(String.valueOf(pricePerUnit) + "/" + normalize(unit));
                priceEntryRepository.save(entry);
            }
        }
    }
    private double normalize(double quantity, String unit) {
        if (unit == null) return 0.0;

        return switch (unit.toLowerCase()) {
            case "ml", "g" -> quantity / 1000.0;
            case "kg", "l", "buc", "role" -> quantity;
            default -> {
                System.out.println("Unknown unit: " + unit);
                yield 0.0;
            }
        };
    }
    private String normalize(String unit) {
        if (unit == null) return "";

        return switch (unit.toLowerCase()) {
            case "l", "ml"-> "l";
            case "kg", "g" -> "kg";
            case "buc" -> "buc";
            case "role" -> "rola";
            default -> {
                System.out.println("Unknown unit: " + unit);
                yield "";
            }
        };
    }

}
