package org.example.market;

import org.example.market.service.CsvImportService;
import org.example.market.service.ProductSubstitutesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }

    //to import from csv files
    @Bean
    public CommandLineRunner importCsvRunner(CsvImportService csvImportService) {
        return args -> {
            csvImportService.importAll();
            System.out.println("Import completat cu succes!");
        };
    }

    @Bean
    public CommandLineRunner pricePerUnitRunner(ProductSubstitutesService productSubstitutesService) {
        return args -> {
            productSubstitutesService.calculatePricePerUnit();
            System.out.println("Preturi per unitate calculate cu succes!");
        };
    }
}
