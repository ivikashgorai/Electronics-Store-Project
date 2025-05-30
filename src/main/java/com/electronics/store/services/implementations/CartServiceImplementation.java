package com.electronics.store.services.implementations;

import com.electronics.store.dtos.AddItemToCartRequest;
import com.electronics.store.dtos.entityDtos.CartDto;
import com.electronics.store.entities.Cart;
import com.electronics.store.entities.CartItem;
import com.electronics.store.entities.Product;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.CartItemRepository;
import com.electronics.store.repositories.CartRepository;
import com.electronics.store.repositories.ProductRepository;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.services.Interfaces.CartServiceInterface;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Primary
public class CartServiceImplementation implements CartServiceInterface {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        if (request.getQuantity() <= 0) {
            throw new BadApiRequestException("Quantity should be more than zero");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCreatedAt(new Date());
            newCart.setCartId(UUID.randomUUID().toString());
            newCart.setCartItems(new HashSet<>());
            return newCart;
        });

        Set<CartItem> items = cart.getCartItems();
        AtomicBoolean updated = new AtomicBoolean(false);

        Set<CartItem> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                item.setTotalPrice(item.getQuantity() * product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toSet());

        cart.setCartItems(updatedItems);

        if (!updated.get()) {
            CartItem newItem = CartItem.builder()
                    .quantity(request.getQuantity())
                    .totalPrice(product.getPrice() * request.getQuantity())
                    .product(product)
                    .cart(cart)
                    .build();
            cart.getCartItems().add(newItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        Hibernate.initialize(updatedCart.getCartItems());
        return mapper.map(updatedCart, CartDto.class);
    }




    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));
//        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));
//        List<CartItem> cartItems = cart.getCartItems();
//        cartItems.remove(cartItem);
//        cart.setCartItems(cartItems);
//        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
        cartItemRepository.delete(cartItem);

    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart not founf for this user"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart not founf for this user"));
        return mapper.map(cart,CartDto.class);
    }
}
