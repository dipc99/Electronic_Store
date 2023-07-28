package com.lcwt.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {

    private Long productId;
    @NotBlank
    @Size(min = 5, max = 100)
    private String title;
    @NotBlank
    @Size(min = 5, max = 1000)
    private String description;
    private int price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String imageName;
    private CategoryDto category;
}