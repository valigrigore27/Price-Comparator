package org.example.market;

import org.example.market.model.BasketItem;
import org.example.market.service.BasketOptimizerService;
import org.example.market.service.CsvImportService;
import org.example.market.service.CustomPriceAlertService;
import org.example.market.service.ProductSubstitutesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }

    @Bean
    public CommandLineRunner importCsvRunner(CsvImportService csvImportService) {
        return args -> {
            csvImportService.importAll();
        };
    }

    @Bean
    public CommandLineRunner pricePerUnitRunner(ProductSubstitutesService productSubstitutesService) {
        return args -> {
            productSubstitutesService.calculatePricePerUnit();
        };
    }

    @Bean
    public CommandLineRunner basketRunner(BasketOptimizerService basketOptimizerService, CustomPriceAlertService customPriceAlertService) {
        return args -> {
            List<BasketItem> basket = List.of(
                    new BasketItem(10, "buc", "ouă"),
                    new BasketItem(1, "kg", "pâine"),
                    new BasketItem(5, "kg", "cartofi"),
                    new BasketItem(1, "l", "suc portocale"),
                    new BasketItem(10, "kg", "zahăr"),
                    new BasketItem(5, "kg",  "cașcaval")
            );
            basketOptimizerService.printShoppingLists(basket);
        };
    }

    @Bean
    public CommandLineRunner priceAlertRunner(CustomPriceAlertService customPriceAlertService) {
        return args -> {
            customPriceAlertService.priceOfProductDroppedBelow("lapte", "lidl", 9.79);
        };
    }


}
