package com.electronics.store.controllers;

import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.entityDtos.CartDto;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.services.cart_service.CartServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartServiceInterface cartServiceInterface;

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @RequestBody AddItemToCartRequest addItemToCartRequest,
            @PathVariable("userId") String userId
            ){
        CartDto cartDto = cartServiceInterface.addItemToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("/{userId}/cart-item/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable("userId") String userId,
            @PathVariable("cartItemId") int cartItemId
    ){
        cartServiceInterface.removeItemFromCart(userId,cartItemId);
        return new ResponseEntity<>(ApiResponseMessage.builder().message("Item removed").success(true).status(HttpStatus.OK).build(),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable("userId") String userId
    ){
        cartServiceInterface.clearCart(userId);
        return new ResponseEntity<>(ApiResponseMessage.builder().message("Cart cleared").success(true).status(HttpStatus.OK).build(),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartOfUser(
            @PathVariable("userId") String userId
    ){
        CartDto cartByUserId = cartServiceInterface.getCartByUserId(userId);
        return new ResponseEntity<>(cartByUserId,HttpStatus.OK);
    }

}
