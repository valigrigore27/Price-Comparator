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
        name = "discounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "store_id", "fromDate", "toDate"})
)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Store store;

    private String date;
    private String fromDate;
    private String toDate;
    private int percentageOfDiscount;

}
