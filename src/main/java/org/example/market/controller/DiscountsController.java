package org.example.market.controller;

import org.example.market.model.Discount;
import org.example.market.service.DiscountsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountsController {

    private final DiscountsService discountsService;

    public DiscountsController(DiscountsService discountsService) {
        this.discountsService = discountsService;
    }

    @GetMapping("/best")
    public List<Discount> getTopDiscounts() {
        return discountsService.getBestDiscounts();
    }
    @GetMapping("/new")
    public List<Discount> getNewDiscounts() {
        return discountsService.getNewDiscounts();
    }

}
