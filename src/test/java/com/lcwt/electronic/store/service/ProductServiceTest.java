package com.lcwt.electronic.store.service;

import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;
import com.lcwt.electronic.store.entities.Product;
import com.lcwt.electronic.store.repositories.ProductRepository;
import com.lcwt.electronic.store.servicesI.ProductService;
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
public class ProductServiceTest {

        @MockBean
        private ProductRepository productRepository;
        @Autowired
        private ModelMapper mapper;
        @Autowired
        private ProductService productService;


        Product product;


        @BeforeEach
        public void init() {
            product = Product.builder().title("iPhone 14").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();
        }

        @Test
        public void createProductTest() {
            // ProductDto productdto = this.mapper.map(product, ProductDto.class);
            Mockito.when(this.productRepository.save(Mockito.any())).thenReturn(product);
            ProductDto product1 = productService.createProduct(mapper.map(product, ProductDto.class));

            System.out.println(product1);

            Assertions.assertNotNull(product1);
            Assertions.assertEquals(product.getTitle(), product1.getTitle());
        }

        @Test
        void updateProduct() {

            ProductDto productDto = ProductDto.builder().title("iPhone 13").description("Launched in 2023").price(60000).discountedPrice(6000).quantity(10).live(true).stock(false).imageName("abcc.png").build();
            Long productId = 11L;

            Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
            ProductDto productDto1 = productService.updateProduct(productDto, productId);
            System.out.println(productDto1.getTitle());

        }

        @Test
        void deleteProduct() {
            Long productId = 12L;
            Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            productService.deleteProduct(productId);

            Mockito.verify(productRepository, Mockito.times(1)).delete(product);
        }

        @Test
        void getAllProduct() {

            Product  product1 = Product.builder().title("Samsung A12").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();
            Product product2= Product.builder().title("Samsung A12").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product> page=new PageImpl<>(products);
            Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
            PageableResponse<ProductDto> allProduct = productService.getAllProduct(1, 1, "title", "asc");
            Assertions.assertEquals(3,allProduct.getContent().size());
        }

        @Test
        void getSingleProduct() {

            Long productId = 12L;
            Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            ProductDto product1 = productService.getSingleProduct(productId);
            Assertions.assertNotNull(product1);
            Assertions.assertEquals(product.getTitle(),product1.getTitle());
        }

        @Test
        void getAllLive() {

            Product  product1 = Product.builder().title("Samsung A12").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();
            Product product2= Product.builder().title("Samsung A12").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product>page=new PageImpl<>(products);
            Mockito.when(productRepository.findByLiveTrue((Pageable) Mockito.any())).thenReturn(page);
            PageableResponse<ProductDto> allProduct = productService.getAllLive(1, 1, "title", "asc");
            Assertions.assertEquals(3,allProduct.getContent().size());

        }

        @Test
        void searchByTitle() {
            Product  product1 = Product.builder().title("Samsung A12").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();
            Product product2= Product.builder().title("Samsung A12").description("Launched in 2022").price(50000).discountedPrice(5000).quantity(12).live(true).stock(false).imageName("abc.png").build();

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product>page=new PageImpl<>(products);
            Mockito.when(productRepository.findByTitleContaining(Mockito.anyString(),Mockito.any())).thenReturn(page);
            PageableResponse<ProductDto> response = productService.searchByTitle("keyword", 1, 2, "title", "asc");
            Assertions.assertEquals(3,response.getContent().size());
        }
}
