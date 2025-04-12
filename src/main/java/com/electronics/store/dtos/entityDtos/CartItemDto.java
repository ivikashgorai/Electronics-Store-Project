package com.electronics.store.dtos.entityDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private ProductDto product;

    private int quantity;

    private int totalPrice;
    //item ke andar cart nhi chahiye so no cart here
}
