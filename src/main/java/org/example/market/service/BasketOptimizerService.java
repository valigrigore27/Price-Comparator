package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.example.market.model.*;
import org.example.market.repository.DiscountRepository;
import org.example.market.repository.PriceEntryRepository;
import org.example.market.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor


public class BasketOptimizerService {

    private final ProductRepository productRepository;
    private final PriceEntryRepository priceEntryRepository;
    private final DiscountRepository discountRepository;

    public Map<String, List<ShoppingListProduct>> optimizeBasket(List<BasketItem> basket) {
        //we need a map like "storename: product details list"
        Map<String, List<ShoppingListProduct>> storeToItems = new HashMap<>();

        //taking all my basket items
        for (BasketItem item : basket) {

            boolean flag = false;
            double withDiscount = 0;

            //all the products from products table found by item name
            List<Product> products = productRepository.findByProductNameContainingIgnoreCase(item.getProductName());

            PriceEntry bestEntry = null;
            Product bestProduct = null;
            double bestPricePerUnit = Double.MAX_VALUE;

            //for each found product searches in price_entries table for taking the best price
            for (Product product : products) {
                List<PriceEntry> entries = priceEntryRepository.findByProduct(product);
                //verifying all appearances of each product
                for (PriceEntry entry : entries) {
                    double pricePerUnit = entry.getPricePerUnit();

                    //verifying if there's a discount for this product
               LocalDate today = LocalDate.now();
                Discount discount = discountRepository
                        .findByProductAndStoreAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                                product, entry.getStore(), today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        );
                    //if yes, we update the pricePerUnit
                if (discount != null) {
                    pricePerUnit = pricePerUnit * (100 - discount.getPercentageOfDiscount()) / 100.0;
                    flag = true;
                    withDiscount = pricePerUnit;
                }

                //if found a lower price (with discount or not), update the best variables
                    if (pricePerUnit < bestPricePerUnit) {
                        bestPricePerUnit = pricePerUnit;
                        bestEntry = entry;
                        bestProduct = product;
                    }
                }
            }
                //finally if there's a best product price store we create 'some product details' object (ShoppingListProduct)
            if (bestEntry != null && bestProduct != null) {
                double total = bestPricePerUnit * item.getQuantity();


                ShoppingListProduct detail = new ShoppingListProduct(
                        item.getQuantity(),
                        item.getUnit(),
                        flag ? item.getProductName() + " (with discount)": item.getProductName(),
                        flag ? withDiscount : bestEntry.getPricePerUnit(),
                        total
                );

                //stores will contain a list of product details with the lowest
                // prices among all products of that type in all stores
                storeToItems
                        .computeIfAbsent(bestEntry.getStore().getStoreName(), k -> new ArrayList<>())
                        .add(detail);
            }
        }


        return storeToItems;
    }

    // calculates the total price across all stores
    public double allStoresTotalPrices(Map<String, List<ShoppingListProduct>> map) {
        double allStoresTotalPrices = 0.0;

        // Iterate through each store's shopping list
        for (List<ShoppingListProduct> shoppingLists : map.values()) {
            for (ShoppingListProduct shoppingList : shoppingLists) {
                // Add each item's total price to the overall total
                allStoresTotalPrices += shoppingList.getTotalPrice();
            }
        }

        return allStoresTotalPrices;
    }

    //prints the optimized shopping lists per store and the total price per store and overall
    public void printShoppingLists(List<BasketItem> basket) {
        Map<String, List<ShoppingListProduct>> result = optimizeBasket(basket);

        // print details for each store
        result.forEach((store, items) -> {
            System.out.println("Store: " + store);
            double total = 0;

            //print each product in the store's shopping list
            for (ShoppingListProduct item : items) {
                System.out.printf(" - %s: %.2f %s x %s = %.2f RON\n",
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnit(),
                        item.getPricePerUnit(),
                        item.getTotalPrice());
                total += item.getTotalPrice();
            }

            System.out.printf("Total %s: %.2f RON\n\n", store, total);
        });

        System.out.println("All stores total prices: " + allStoresTotalPrices(result) + " RON");
        System.out.println("\n-------------------------------\n");
    }

}
