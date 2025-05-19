package org.example.market.service;

import org.example.market.model.Discount;
import org.example.market.repository.DiscountRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountsService {
    private final DiscountRepository discountRepository;

    public DiscountsService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<Discount> getBestDiscounts() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // best 10 discounts but not for today specially
//        return discountRepository.findAll().stream()
//                .sorted(Comparator.comparingInt(Discount::getPercentageOfDiscount).reversed())
//                .limit(10)
//                .collect(Collectors.toList());

        return discountRepository.findAll().stream()
                .filter(d -> {
                    try {
                        LocalDate from = LocalDate.parse(d.getFromDate(), formatter);
                        LocalDate to = LocalDate.parse(d.getToDate(), formatter);
                        return !from.isAfter(today) && !to.isBefore(today);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .sorted(Comparator.comparingInt(Discount::getPercentageOfDiscount).reversed())
                .limit(10)
                .collect(Collectors.toList());

    }

    public List<Discount> getNewDiscounts() {
        LocalDate now = LocalDate.now();
        LocalDate yesterday = now.minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return discountRepository.findAll().stream()
                .filter(d -> {
                    try {
                        LocalDate from = LocalDate.parse(d.getFromDate(), formatter);
                        return from.isAfter(yesterday);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(Discount::getFromDate).reversed())
                .collect(Collectors.toList());
    }

}

