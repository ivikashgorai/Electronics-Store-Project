package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="categories")
public class Category {

    @Id
    @Column(name="id")
    private String categoryId;
    @Column(name="category_title",length = 60,nullable = false)
    private String title;
    @Column(name="category_description")
    private String description;
    @Column(name="category_coverImage")
    private String coverImage;

    //one category can have multiple products so we need list of product
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY) // one category can have multiple product
    //cascade All means if category get updated product also will get updated,
    // if category get delete product will also get deleted
    // fetch type lazy means if we fetch category then product will not get fetch auto.,product will be get fetched on demand
    //mappedBy means to manage the mapping, a category column will be there in Product table,we use this so to avoid extra tables
    //mapped by mean “This side of the relationship is not the owner, the other side owns the foreign key in the DB.”
    private List<Product> productList = new ArrayList<>();
}
