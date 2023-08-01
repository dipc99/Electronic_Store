package com.lcwt.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long cartItemId;
    private ProductDto product;
    private int quantity;
    private int totalPrice;


}
