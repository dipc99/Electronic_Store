package com.lcwt.electronic.store.service;

import com.lcwt.electronic.store.dtos.CategoryDto;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.entities.Category;
import com.lcwt.electronic.store.repositories.CategoryRepo;
import com.lcwt.electronic.store.servicesI.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private ModelMapper mapper;

    @MockBean
    private CategoryRepo categoryRepository;

    @Autowired
    private CategoryService categoryService;

    Category category;

    @BeforeEach
    public void init() {

        category = Category.builder()
                .categoryId(111L)
                .title("Category!")
                .description("This is first Category")
                .coverImage("abc.png")
                .build();
    }

    @Test
    public void createCategory() {

        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto category1 = categoryService.createCategory(mapper.map(category, CategoryDto.class));
        System.out.println(category1);

        Assertions.assertNotNull(category1);
        Assertions.assertEquals("Category!", category1.getTitle());
    }

    @Test
    public void updateCategoryTest() {

        CategoryDto categoryDto = CategoryDto.builder()
                .title("Category!")
                .description("This is first Category")
                .coverImage("abc.png")

                .build();
        Long categoryId = 111L;
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto updateCategory = categoryService.updaetCategory(categoryDto, categoryId);
        System.out.println(updateCategory.getTitle());

    }

    @Test
    public void getCategoriesTest() {
        Category category1 = Category.builder()
                .categoryId(111L)
                .title("Category!")
                .description("This is first Category")
                .coverImage("abc.png")
                .build();
        Category category2 = Category.builder()
                .categoryId(112L)
                .title("Category2!")
                .description("This is second Category")
                .coverImage("abc.png")
                .build();

        List<Category> list = Arrays.asList(category1, category2);
        Page<Category> page = new PageImpl<>(list);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<CategoryDto> allCategories = categoryService.getCategories(1, 2, "title", "desc");
        Assertions.assertEquals(2, allCategories.getContent().size(), " test case failed due to not size validate");
    }

    @Test
    public void deleteCategoryTest(){

        Long categoryId = 111L;
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(categoryId);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }
    @Test
    public void getCategoryByIdTest(){

        Long categoryId = 111L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle());
    }

}
