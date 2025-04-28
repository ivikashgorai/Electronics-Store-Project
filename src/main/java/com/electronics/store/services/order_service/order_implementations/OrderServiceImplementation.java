package com.electronics.store.services.order_service.order_implementations;

import com.electronics.store.dtos.entityDtos.OrderDto;
import com.electronics.store.dtos.entityDtos.OrderItemDto;
import com.electronics.store.entities.*;
import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.CartRepository;
import com.electronics.store.repositories.OrderItemRepository;
import com.electronics.store.repositories.OrderRepository;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.services.order_service.OrderServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class OrderServiceImplementation implements OrderServiceInterface {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(OrderDto orderDto, String userId,String cartId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("Cart not found"));

        Set<CartItem> cartItems = cart.getCartItems();

        if(cartItems.isEmpty()){
            throw new BadApiRequestException("Cart is empty");
        }

        Order order = mapper.map(orderDto, Order.class);
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderedDate(new Date());
        order.setUser(user);
        order.setOrderAmount(0); // temporary

// Step 1: Save the order first to get a valid order_id
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        long orderAmount = 0;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(savedOrder); // use savedOrder here
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderAmount += orderItem.getTotalPrice();
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

// Step 2: Update saved order with order items and amount
        savedOrder.setOrderItems(orderItems);
        savedOrder.setOrderAmount(orderAmount);
        Order finalOrder = orderRepository.save(savedOrder);

// Clear the cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return mapper.map(finalOrder, OrderDto.class);

    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getAllOrderOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order o: orders){
            ordersDto.add(mapper.map(o,OrderDto.class));
        }

        return ordersDto;
    }

    @Override
    public List<OrderDto> getOrders() {
        List<Order> allOrders = orderRepository.findAll();
        List<OrderDto> list = allOrders.stream().map(order ->
                mapper.map(order, OrderDto.class)
        ).toList();

        return list;
    }


}
