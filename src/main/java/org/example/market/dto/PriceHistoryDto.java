package org.example.market.dto;

public record PriceHistoryDto(
        String date,
        double price,
        String currency,
        String storeName
) {
}
