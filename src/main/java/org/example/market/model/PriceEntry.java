package org.example.market.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "price_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "store_id", "date"})
)
public class PriceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Store store;

    private String date;
    private double price;
    private String currency;
    private double pricePerUnit;

}
