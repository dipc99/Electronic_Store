package com.lcwt.electronic.store.servicesI;

import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;

public interface ProductService {

    //Create Product
    ProductDto createProduct(ProductDto productDto);

    //Update Product
    ProductDto updateProduct(ProductDto productDto, Long productId);

    //Delete Product

    void deleteProduct(Long productId);

    //Get All Products
    PageableResponse<ProductDto> getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //Get Single Product
    ProductDto getSingleProduct(Long productId);

    //get all Live
    PageableResponse<ProductDto> getAllLive(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //search Product
    PageableResponse<ProductDto> searchByTitle(String title, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //create product with category

    ProductDto createWithCategory(ProductDto productDto,Long categoryId);

}
