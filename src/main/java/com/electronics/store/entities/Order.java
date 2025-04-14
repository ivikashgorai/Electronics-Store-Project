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
@Table(name="orders")
public class Order {

    @Id
    private String orderId; //this is set by us

    //PENDING,DISPATCHED,DELIVERED
    private enum orderStatus{
        PENDING,
        DISPATCHED,
        DELIVERED
    }

    private enum paymentStatus{
        CASH_ON_DELIVERY,
        PAID
    }

    private long orderAmount;

    @Column(length = 200)
    private String deliveryAddress;

    @Column(length = 13) //including +91
    private String deliveryPhone;

    private String deliveryName;

    private Date orderedDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    //ek order mein kya kya hai(order item)
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

}
