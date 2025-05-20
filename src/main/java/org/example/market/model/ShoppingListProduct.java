package org.example.market.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListProduct {
    private double quantity;
    private String unit;
    private String productName;
    private double pricePerUnit;
    private double totalPrice;
}
