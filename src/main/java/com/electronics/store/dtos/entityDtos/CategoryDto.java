package com.electronics.store.dtos.entityDtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {


    private String categoryId;

    @NotBlank(message = "Title required")
    @Size(min = 3,max = 20)
    private String title;

    @NotBlank(message = "description required")
    private String description;

    @NotBlank
    private String coverImage;
}
