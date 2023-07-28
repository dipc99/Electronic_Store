package com.lcwt.electronic.store.servicesI.serviceImpl;

import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;
import com.lcwt.electronic.store.entities.Category;
import com.lcwt.electronic.store.entities.Product;
import com.lcwt.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.helper.Helper;
import com.lcwt.electronic.store.repositories.CategoryRepo;
import com.lcwt.electronic.store.repositories.ProductRepository;
import com.lcwt.electronic.store.servicesI.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private static Logger log= LoggerFactory.getLogger(ProductServiceImpl.class);
    
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepo categoryRepo;
    /**
     * @author DipaliC.
     * @implNote Methods for Product Implementation
     * @param productDto
     * @return
     */

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Initiating the dao call for save product data");
        //product ID
        Product products = this.modelMapper.map(productDto, Product.class);
//        Long string = Long.valueOf(UUID.randomUUID().toString());
//        products.setProductId(products.getProductId());
        //set date
     products.setAddedDate(new Date());
        Product save = this.productRepo.save(products);
        log.info("Request Completed of the dao call for save the product data");
        return this.modelMapper.map(save, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long productId) {
        log.info("Initiating the dao call for update product data:{}",productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT, AppConstants.PRODUCT_ID, productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setImageName(productDto.getImageName());
        Product save = this.productRepo.save(product);
        log.info("Request Completed of the dao call for update the product data:{}",productId);
        return this.modelMapper.map(save, ProductDto.class);

    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Initiating the dao call for delete product data:{}",productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT, AppConstants.PRODUCT_ID, productId));
        log.info("Request Completed of the dao call for deleteProduct data:{}",productId);
        this.productRepo.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initiating the dao call for getAll product data");
        Sort sort=(sortDir.equalsIgnoreCase(AppConstants.SORT_DESC)) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = this.productRepo.findAll(pageable);
        log.info("Request Completed of the dao call for getAllProduct the product data");
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto getSingleProduct(Long productId) {
        log.info("Initiating the dao call for getSingle product data:{}",productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT, AppConstants.PRODUCT_ID, productId));
        log.info("Request Completed of the dao call for getSingleProduct the product data:{}",productId);
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initiating the dao call for getAll Live product data");
        Sort sort=(sortDir.equalsIgnoreCase(AppConstants.SORT_DESC)) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = this.productRepo.findByLiveTrue(pageable);
        log.info("Request Completed of the dao call for getAllLive the product data");
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initiating the dao call for searchByTitle product data");
        Sort sort=(sortDir.equalsIgnoreCase(AppConstants.SORT_DESC)) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = this.productRepo.findByTitleContaining(title,pageable);
        log.info("Request Completed of the dao call for searchByTitle the product data:{}",title);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, Long categoryId) {
        log.info("Request initiate of the dao call for save the product data with categoryId:{}",categoryId);
        //fetch the category from DB:
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND, "", categoryId));
        Product products = this.modelMapper.map(productDto, Product.class);
        products.setAddedDate(new Date());
        products.setCategory(category);
        Product save = this.productRepo.save(products);
        log.info("Request Completed of the dao call for save the product data with categoryId:{}",categoryId);
        return this.modelMapper.map(save, ProductDto.class);
    }
}
