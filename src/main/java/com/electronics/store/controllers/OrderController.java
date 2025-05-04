package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.OrderDto;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.services.Interfaces.OrderServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceInterface orderServiceInterface;

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')") // method level security  NORMAL,ADMIN user can access
    @PostMapping("/cart/{cartId}/user/{userId}")
    public ResponseEntity<OrderDto> createOrder(
            @Valid
            @RequestBody OrderDto orderDto,
            @PathVariable("userId") String userId,
            @PathVariable("cartId") String cartId
    ){
        OrderDto order = orderServiceInterface.createOrder(orderDto, userId, cartId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(
            @PathVariable("orderId") String orderId
    ){
        orderServiceInterface.removeOrder(orderId);
        return new ResponseEntity<>(ApiResponseMessage.builder().message("deleted succesfully").success(true).status(HttpStatus.OK).build(),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(
            @PathVariable("userId") String userId
    ){
        List<OrderDto> allOrderOfUser = orderServiceInterface.getAllOrderOfUser(userId);
        return new ResponseEntity<>(allOrderOfUser,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(
    ){
        List<OrderDto> allOrderOfUser = orderServiceInterface.getOrders();
        return new ResponseEntity<>(allOrderOfUser,HttpStatus.OK);
    }



}
