package com.electronics.store.services.Interfaces;

import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.entityDtos.CartDto;

public interface CartServiceInterface {

    //add cart item to cart
    //case 1 : if cart is not available for that user then we create the cart
    //case 2 : if cart available then directly add the cart item to it
    //userId se cart mil jayega user ka
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId,int cartItemId);

    //remove all item from cart
    void clearCart(String userId); //user ka cart ka data remove kar denge

    CartDto getCartByUserId(String userId);

}
