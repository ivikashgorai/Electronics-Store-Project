package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.OrderDto;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.services.Interfaces.OrderServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Management", description = "Operations related to orders")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceInterface orderServiceInterface;

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PostMapping("/cart/{cartId}/user/{userId}")
    @Operation(summary = "Create a new order for a user and cart")
    public ResponseEntity<OrderDto> createOrder(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order details")
            @RequestBody OrderDto orderDto,
            @Parameter(description = "User ID", in = ParameterIn.PATH)
            @PathVariable("userId") String userId,
            @Parameter(description = "Cart ID", in = ParameterIn.PATH)
            @PathVariable("cartId") String cartId
    ){
        OrderDto order = orderServiceInterface.createOrder(orderDto, userId, cartId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order by ID")
    public ResponseEntity<ApiResponseMessage> removeOrder(
            @Parameter(description = "Order ID to delete", in = ParameterIn.PATH)
            @PathVariable("orderId") String orderId
    ){
        orderServiceInterface.removeOrder(orderId);
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .message("deleted successfully")
                        .success(true)
                        .status(HttpStatus.OK)
                        .build(),
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get all orders of a specific user")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(
            @Parameter(description = "User ID to fetch orders for", in = ParameterIn.PATH)
            @PathVariable("userId") String userId
    ){
        List<OrderDto> allOrderOfUser = orderServiceInterface.getAllOrderOfUser(userId);
        return new ResponseEntity<>(allOrderOfUser,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all orders (admin only)")
    public ResponseEntity<List<OrderDto>> getOrders(){
        List<OrderDto> allOrders = orderServiceInterface.getOrders();
        return new ResponseEntity<>(allOrders,HttpStatus.OK);
    }
}
