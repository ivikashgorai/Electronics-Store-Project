package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="products")
public class Product {

    @Id
    @Column(name="id")
    private String productId;

    @Column(length=30)
    private String title;

    @Column(length = 10000)
    private String description;

    private int price;

    private int discountedPrice;

    private int quantity;

    private Date addedDate;

    private boolean live;

    private boolean stock;

    private String productImageName;

    //one product can be in only one category so
    @ManyToOne(fetch = FetchType.EAGER) // when product will get fetch, category will also get fetch
    @JoinColumn(name = "category_id") // join column means product table mein ek category id karke column rahega jisme catgeory(category id_) rahaga uss product ka
    private Category category;
}
