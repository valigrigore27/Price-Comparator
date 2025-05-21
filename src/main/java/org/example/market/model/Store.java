package org.example.market.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stores")
public class Store {

    @Id
    private String storeName;
}
