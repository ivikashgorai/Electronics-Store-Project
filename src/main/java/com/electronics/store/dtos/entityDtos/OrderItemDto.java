package com.electronics.store.dtos.entityDtos;

import com.electronics.store.entities.Order;
import com.electronics.store.entities.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private int quantity;

    private int totalPrice;

    private ProductDto product;

}
