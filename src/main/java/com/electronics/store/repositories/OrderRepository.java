package com.electronics.store.repositories;

import com.electronics.store.entities.Order;
import com.electronics.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<List<Order>> findByUser(User user);
}
