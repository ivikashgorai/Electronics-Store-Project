package com.electronics.store.repositories;

import com.electronics.store.entities.Cart;
import com.electronics.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);
}
