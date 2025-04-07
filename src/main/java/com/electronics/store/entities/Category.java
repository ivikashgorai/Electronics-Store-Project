package com.electronics.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
}
