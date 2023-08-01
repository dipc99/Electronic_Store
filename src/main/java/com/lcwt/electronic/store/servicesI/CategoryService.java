package com.lcwt.electronic.store.servicesI;

import com.lcwt.electronic.store.dtos.CategoryDto;
import com.lcwt.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updaetCategory(CategoryDto categoryDto, Long categoryId);

    void deleteCategory(Long categoryId);

    CategoryDto getCategory(Long categoryId);

    PageableResponse<CategoryDto> getCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

}
