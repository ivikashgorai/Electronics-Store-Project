package com.electronics.store.services.Interfaces;

import com.electronics.store.dtos.entityDtos.OrderDto;

import java.util.List;

public interface OrderServiceInterface {

    // create order
    OrderDto createOrder(OrderDto orderDto, String userId,String cartId);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getAllOrderOfUser(String userId);

    //get orders
    List<OrderDto> getOrders();
}
