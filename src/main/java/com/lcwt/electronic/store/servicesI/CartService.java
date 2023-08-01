package com.lcwt.electronic.store.servicesI;

import com.lcwt.electronic.store.dtos.AddItemToCartRequest;
import com.lcwt.electronic.store.dtos.CartDto;

public interface CartService {

    //add item to cart
    //case1: cart for user is not available :we will create the cart and then add the item
    //case2: cart available add the item to cart
    CartDto addItemToCart(Long userId, AddItemToCartRequest request);

    //remove item to cart
    void removeItemFromCart(Long userId, Long cartItem);

    //remove all items from cart
    void clearCart(Long userId);

    CartDto getCartByUser(Long userId);

}
