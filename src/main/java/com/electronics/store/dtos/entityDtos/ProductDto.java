package com.electronics.store.dtos.entityDtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDto {


    private String productId;

    @NotBlank(message = "Title required")
    @Size(min = 3,max = 30)
    private String title;
    @NotBlank(message = "description required")
    @Size(min = 3,max = 10000)
    private String description;
    private int price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
}
