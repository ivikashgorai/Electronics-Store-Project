package com.electronics.store.controllers;

import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.entityDtos.CartDto;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.services.Interfaces.CartServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart Management", description = "Operations related to shopping carts")
public class CartController {

    @Autowired
    private CartServiceInterface cartServiceInterface;

    @PostMapping("/{userId}")
    @Operation(summary = "Add item to cart", description = "Add a new item to the user's cart")
    public ResponseEntity<CartDto> addItemToCart(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Item details to add")
            @RequestBody AddItemToCartRequest addItemToCartRequest,
            @Parameter(description = "User ID", in = ParameterIn.PATH) @PathVariable("userId") String userId
    ){
        CartDto cartDto = cartServiceInterface.addItemToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/cart-item/{cartItemId}")
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the user's cart")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @Parameter(description = "User ID", in = ParameterIn.PATH) @PathVariable("userId") String userId,
            @Parameter(description = "Cart Item ID to remove", in = ParameterIn.PATH) @PathVariable("cartItemId") int cartItemId
    ){
        cartServiceInterface.removeItemFromCart(userId,cartItemId);
        return new ResponseEntity<>(ApiResponseMessage.builder().message("Item removed").success(true).status(HttpStatus.OK).build(),HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Clear cart", description = "Remove all items from the user's cart")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @Parameter(description = "User ID", in = ParameterIn.PATH) @PathVariable("userId") String userId
    ){
        cartServiceInterface.clearCart(userId);
        return new ResponseEntity<>(ApiResponseMessage.builder().message("Cart cleared").success(true).status(HttpStatus.OK).build(),HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get cart of user", description = "Fetch the current cart of the user")
    public ResponseEntity<CartDto> getCartOfUser(
            @Parameter(description = "User ID", in = ParameterIn.PATH) @PathVariable("userId") String userId
    ){
        CartDto cartByUserId = cartServiceInterface.getCartByUserId(userId);
        return new ResponseEntity<>(cartByUserId,HttpStatus.OK);
    }

}
