package com.lcwt.electronic.store.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;
import com.lcwt.electronic.store.entities.Product;
import com.lcwt.electronic.store.servicesI.ProductService;
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
public class ProductControllerTest {
         @MockBean
        private ProductService productService;
        @Autowired
        private ModelMapper mapper;
        @Autowired
        private MockMvc mockMvc;
        private Product product;
        @BeforeEach
        void init (){
            product = Product.builder()
                    .title("iPhone ")
                    .description(" Mobile phones ")
                    .live(true)
                    .imageName(" mobile.png")
                    .discountedPrice(65000)
                    .price(75000)
                    .quantity(25)
                    .stock(true).build();
        }
        @Test
        void createProductTest() throws Exception {
            ProductDto productDto = this.mapper.map(product, ProductDto.class);
            Mockito.when(productService.createProduct(Mockito.any())).thenReturn(productDto);
            this.mockMvc.perform(
                            MockMvcRequestBuilders.post("/api/products")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(convertObjectToJsonString(product))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").exists());
        }

        private String convertObjectToJsonString(Object product) {

            try {
                return new ObjectMapper().writeValueAsString(product);
            }catch(Exception e ){
                e.printStackTrace();
                return  null;
            }
        }

        @Test
        void updateProductTest() throws Exception {
            Long productId =11L;
            ProductDto productdto = ProductDto.builder()
                    .title(" Mobile phones ")
                    .description(" Mobile phones")
                    .live(true)
                    .imageName(" phone.png")
                    .discountedPrice(75000)
                    .price(95000)
                    .stock(false).build();
            Mockito.when(productService.updateProduct(Mockito.any(),Mockito.anyLong())).thenReturn(productdto);
            this.mockMvc.perform(
                            MockMvcRequestBuilders.put("/api/products/"+productId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(convertObjectToJsonString(product))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").exists());
        }

        @Test
        void getProductByIdTest() throws Exception {

            Long productId =11L;
            ProductDto productDto = this.mapper.map(product, ProductDto.class);
            Mockito.when(productService.getSingleProduct(Mockito.anyLong())).thenReturn(productDto);
            this.mockMvc.perform(
                            MockMvcRequestBuilders.get("/api/products/"+productId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").exists());
        }

        @Test
        void deleteProductTest() throws Exception {
            Long productId =11L;
            Mockito.doNothing().when(productService).deleteProduct(Mockito.anyLong());
            this.mockMvc.perform(
                            MockMvcRequestBuilders.delete("/api/products/" + productId))
                    .andDo(print())
                    .andExpect(status().isOk());
            Mockito.verify(productService, Mockito.times(1)).deleteProduct(productId);
        }

        @Test
        void getAllProductsTest() throws Exception {
            ProductDto product1 = ProductDto.builder()
                    .title(" TV")
                    .description("Canon TV ")
                    .live(true)
                    .imageName(" canon.png")
                    .discountedPrice(20000)
                    .price(25000)
                    .stock(false).build();

            ProductDto product2 = ProductDto.builder()
                    .title(" iPhone ")
                    .description(" iPhone Mobile")
                    .live(true)
                    .imageName(" phone.png")
                    .discountedPrice(75000)
                    .price(95000)
                    .stock(false).build();

            ProductDto product3 = ProductDto.builder()
                    .title("Sports")
                    .description("Cricket Sport Kit")
                    .live(true)
                    .imageName(" cricket.png")
                    .discountedPrice(45000)
                    .price(55000)
                    .stock(false).build();

            PageableResponse<ProductDto> pageableResponse = new PageableResponse<>();
            pageableResponse.setLastPage(false);
            pageableResponse.setTotalElement(200);
            pageableResponse.setPageNumber(5);
            pageableResponse.setContent(Arrays.asList(product1,product2,product3));
            pageableResponse.setTotalPages(20);
            pageableResponse.setPageSize(10);
            Mockito.when(productService.getAllProduct(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
            this.mockMvc.perform(
                            MockMvcRequestBuilders.get("/api/products/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void getAllLiveProductsTest() throws Exception {
            ProductDto product2 = ProductDto.builder()
                    .title(" iPhone ")
                    .description(" iPhone Mobile")
                    .live(true)
                    .imageName(" phone.png")
                    .discountedPrice(75000)
                    .price(95000)
                    .stock(false).build();

            ProductDto product3 = ProductDto.builder()
                    .title("Sports")
                    .description("Cricket Sport Kit")
                    .live(true)
                    .imageName(" cricket.png")
                    .discountedPrice(45000)
                    .price(55000)
                    .stock(false).build();
            PageableResponse<ProductDto> pageableResponse = new PageableResponse<>();
            pageableResponse.setLastPage(false);
            pageableResponse.setTotalElement(200);
            pageableResponse.setPageNumber(50);
            pageableResponse.setContent(Arrays.asList(product2,product3));
            pageableResponse.setTotalPages(20);
            pageableResponse.setPageSize(10);

            Mockito.when(productService.getAllLive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
            this.mockMvc.perform(
                            MockMvcRequestBuilders.get("/api/products/live")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void searchProductByTitleTest() throws Exception {
            String subtitle = "kit";
            ProductDto product1 = ProductDto.builder()
                    .title(" cricket kit  ")
                    .description(" cricket kits ")
                    .live(true)
                    .imageName(" bat.png")
                    .discountedPrice(65000)
                    .price(75000)
                    .stock(false).build();

            ProductDto product2 = ProductDto.builder()
                    .title(" Mobile phones ")
                    .description(" Mobile phones")
                    .live(true)
                    .imageName(" phone.png")
                    .discountedPrice(75000)
                    .price(95000)
                    .stock(false).build();

            PageableResponse<ProductDto> pageableResponse = new PageableResponse<>();
            pageableResponse.setLastPage(false);
            pageableResponse.setTotalElement(2000);
            pageableResponse.setPageNumber(50);
            pageableResponse.setContent(Arrays.asList(product1,product2));
            pageableResponse.setTotalPages(200);
            pageableResponse.setPageSize(20);

            Mockito.when(productService.searchByTitle(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
            this.mockMvc.perform(
                            MockMvcRequestBuilders.get("/api/products/search/"+subtitle)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isFound());
        }
    }

