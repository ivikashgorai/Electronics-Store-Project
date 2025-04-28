package com.electronics.store.services.order_service;

import com.electronics.store.dtos.entityDtos.OrderDto;
import com.electronics.store.entities.Order;
import com.electronics.store.entities.OrderItem;

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
