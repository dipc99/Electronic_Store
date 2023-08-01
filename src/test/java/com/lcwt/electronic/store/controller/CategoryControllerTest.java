package com.lcwt.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwt.electronic.store.dtos.CategoryDto;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;
import com.lcwt.electronic.store.entities.Category;
import com.lcwt.electronic.store.servicesI.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @MockBean
    private CategoryService categoryServiceI;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MockMvc mockmvc;

    private Category category;

    @BeforeEach
    void init (){
        category = Category.builder()
                .title("category")
                .description("Category for product")
                .coverImage("abc.png").build();
    }
    @Test
    void createCategory() throws Exception {
        CategoryDto categoryDto= this.mapper.map(category, CategoryDto.class);
        Mockito.when(categoryServiceI.createCategory(Mockito.any())).thenReturn(categoryDto);
        this.mockmvc.perform(
                        MockMvcRequestBuilders.post("/api/category/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    private String convertObjectToJsonString(Object category) {

        try {
            return new ObjectMapper().writeValueAsString(category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Test
    void updateCategory() throws Exception {
        Long categoryId =20L;
        CategoryDto categoryDto = CategoryDto.builder()
                 .title("category")
                .description("Category for product")
                .coverImage("abc.png").build();

        Mockito.when(categoryServiceI.updaetCategory(Mockito.any(),Mockito.anyLong())).thenReturn(categoryDto);
        this.mockmvc.perform(
                        MockMvcRequestBuilders.put("/api/category/"+categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void deleteCategory() throws Exception {
        Long categoryId =20L;
        Mockito.doNothing().when(categoryServiceI).deleteCategory(Mockito.anyLong());
        this.mockmvc.perform(
                        MockMvcRequestBuilders.delete("/api/category/" +categoryId))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(categoryServiceI, Mockito.times(1)).deleteCategory(categoryId);
    }

    @Test
    void getCategoryById() throws Exception {
        Long categoryId =20L;
        CategoryDto categoryDto = this.mapper.map(category, CategoryDto.class);
        Mockito.when(categoryServiceI.getCategory(Mockito.anyLong())).thenReturn(categoryDto);
        this.mockmvc.perform(
                        MockMvcRequestBuilders.get("/api/category/"+categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
              //  .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void getAllCategories() throws Exception {
        CategoryDto categoryDto1 = CategoryDto.builder()
                .title("category")
                .description("Category for product")
                .coverImage("abc.png").build();
        CategoryDto categoryDto2 = CategoryDto.builder()
                .title("category2")
                .description("Category for product")
                .coverImage("abc.png").build();
        CategoryDto categoryDto3 = CategoryDto.builder()
                .title("category3")
                .description("Category for product")
                .coverImage("abc.png").build();
        PageableResponse<CategoryDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setLastPage(false);
        pageableResponse.setTotalElement(200);
        pageableResponse.setPageNumber(50);
        pageableResponse.setContent(Arrays.asList(categoryDto1, categoryDto2, categoryDto3));
        pageableResponse.setTotalPages(20);
        pageableResponse.setPageSize(10);

        Mockito.when(categoryServiceI.getCategories(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
        this.mockmvc.perform(
                        MockMvcRequestBuilders.get("/api/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
