package com.lcwt.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long categoryId;
    @NotBlank
    @Size(min = 4)
    private String title;
    @NotBlank
    @Size(min = 10)
    private String description;
    private String coverImage;
}
