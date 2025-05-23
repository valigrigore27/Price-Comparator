package org.example.market.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    private String productId;

    private String productName;
    private String productCategory;
    private String brand;
    private double packageQuantity;
    private String packageUnit;
}
