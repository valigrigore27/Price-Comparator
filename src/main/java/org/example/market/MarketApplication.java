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
                    //best prices should be from kaufland without discount
                    new BasketItem(1, "l", "suc portocale"),
                    new BasketItem(5, "kg",  "cașcaval"),
                    //kaufland too but with discount (see 'kaufland_discounts_2025-05-20.csv')
                    new BasketItem(3, "l", "ulei"),
                    new BasketItem(3, "kg", "morcovi"),
                    new BasketItem(1, "kg", "pâine"),

                    //best prices from lidl without discount
                    new BasketItem(5, "kg", "cartofi"),
                    // see 'lidl_discounts_2025-05-20.csv'
                    new BasketItem(7, "kg", "piept pui"),
                    new BasketItem(2, "kg", "banane"),
                    new BasketItem(0.5, "kg", "file somon"),

                    //best prices from profi without discount
                    new BasketItem(10, "buc", "ouă"),
                    // see 'profi_discounts_2025-05-20.csv'
                    new BasketItem(0.5, "kg", "telemea"),
                    new BasketItem(0.5, "kg", "zahăr tos")
            );
            basketOptimizerService.printShoppingLists(basket);
        };
    }

    @Bean
    public CommandLineRunner priceAlertRunner(CustomPriceAlertService customPriceAlertService) {
        return args -> {
            customPriceAlertService.priceOfProductDroppedBelow("pâine albă", "kaufland", 2);
        };
    }


}
