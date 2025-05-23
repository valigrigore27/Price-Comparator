package org.example.market.controller;

import org.example.market.dto.PriceHistoryDto;
import org.example.market.service.PriceHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/price-history")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/{productName}")
    public List<PriceHistoryDto> getHistory(
            @PathVariable String productName,
            @RequestParam Optional<String> productCategory,
            @RequestParam Optional<String> brand,
            @RequestParam Optional<String> storeName) {
        return priceHistoryService.getPriceHistory(productName, productCategory,  brand, storeName);
    }
}
