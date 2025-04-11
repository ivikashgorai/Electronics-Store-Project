package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cart")
public class Cart {

    @Id
    private String cartId;

    private Date createdAt;

    @OneToOne // one cart have only one user
    private User user;

    //see product and category to more about mapping
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER) // one cart has many cart item
    private List<CartItem> cartItems = new ArrayList<>();

}
