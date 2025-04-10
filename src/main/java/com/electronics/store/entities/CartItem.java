package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto. increment hoga
    private int cartItemId;

    @OneToOne // one directional mapping will be here
    //this means will want to know only which product is in this cart item,
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private int productPrice; //price of one product

    //mapping for this cart item belong to which cart
    @ManyToOne(fetch = FetchType.LAZY) //but sare cart items ek cart se belong kar sakte hai
    //lazy - cart item nikalne pe cart nhi chahiye
    @JoinColumn(name = "cart_id") // gives name of cart column which is foreign key,
    // even if we don't write join column jpa will amke it with auto. naming the column
    //mapped by mean “This side of the relationship is not the owner, the other side owns the foreign key in the DB.”
    private Cart cart;

}
