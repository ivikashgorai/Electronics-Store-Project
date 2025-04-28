package com.electronics.store.dtos.entityDtos;

import com.electronics.store.entities.OrderItem;
import com.electronics.store.entities.User;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private String orderId; //this is set by us

    //PENDING,DISPATCHED,DELIVERED
    private String orderStatus;

    //  CASH_ON_DELIVERY,
    //NOT_PAID
    //        PAID
    private String paymentStatus;

    private long orderAmount;

    private String deliveryAddress;

    private String deliveryPhone;

    private String deliveryName;

    private Date deliveredDate;

    private UserDto user;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
