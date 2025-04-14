package com.electronics.store.entities;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    private int quantity;

    private int totalPrice;

    @ManyToOne //bhut sara order item mein same product ho sakta hai
    @JoinColumn(name="product_id",nullable = false,unique = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="order_id",nullable = false)
    private Order order;
}
