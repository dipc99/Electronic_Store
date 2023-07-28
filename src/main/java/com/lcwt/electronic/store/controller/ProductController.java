package com.lcwt.electronic.store.controller;

import com.lcwt.electronic.store.dtos.ImageResponse;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;
import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.exceptions.ApiResponseMessage;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.servicesI.FileService;
import com.lcwt.electronic.store.servicesI.ProductService;
import com.lcwt.electronic.store.servicesI.serviceImpl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class ProductController {
    private static Logger log= LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;

    /**
     * @author DipaliC.
     * @apiNote Methods for Product Controller
     * @param productDto
     * @return
     */

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        log.info("Request Entering for createProduct");
        ProductDto product = this.productService.createProduct(productDto);
        log.info("Request Completed of CreateProduct....");
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable Long productId){
        log.info("Initiating the request for update product data:{}",productId);
        ProductDto product = this.productService.updateProduct(productDto,productId);
        log.info("Request Completed for update the product data:{}",productId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable Long productId){
        log.info("Initiating request for getSingle product data:{}",productId);
        ProductDto product = this.productService.getSingleProduct(productId);
        log.info("Request Completed for getSingleProduct the product data:{}",productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam (value = "pageSize",defaultValue = "3",required = false) Integer pageSize,
            @RequestParam (value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam (value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){

        log.info("Initiating request for getAll product data");
        PageableResponse<ProductDto> allProduct = this.productService.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        log.info("Request Completed  getAllLive the product data");
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable Long productId){
        log.info("Initiating request for delete product data:{}",productId);
         this.productService.deleteProduct(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(AppConstants.PRODUCT_DELETE).status(HttpStatus.OK).success(true).build();
        log.info("Request Completed for deleteProduct data:{}",productId);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/products/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam (value = "pageSize",defaultValue = "3",required = false) Integer pageSize,
            @RequestParam (value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam (value = "sortBy", defaultValue = "userName",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
       log.info("Initiating request for getAllLive product data");
       PageableResponse<ProductDto> allProduct = this.productService.getAllLive(pageNumber,pageSize,sortBy,sortDir);
        log.info("Request Completed for getAllLive the product data");
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    @GetMapping("/products/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam (value = "pageSize",defaultValue = "3",required = false) Integer pageSize,
            @RequestParam (value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam (value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
        log.info("Initiating request for searchByTitle product data");
        PageableResponse<ProductDto> allProduct = this.productService.searchByTitle(query,pageNumber,pageSize,sortBy,sortDir);
        log.info("Request Completed for searchByTitle the product data");
        return new ResponseEntity<>(allProduct, HttpStatus.FOUND);
    }
    @PostMapping("/product/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
                                                        @PathVariable Long productId,
                                                        @RequestPart("productImage")MultipartFile image
                                                        ) throws IOException {

        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto= productService.getSingleProduct(productId);
        productDto.setImageName(fileName);
        ProductDto updateProduct = productService.updateProduct(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updateProduct.getImageName()).message(AppConstants.P_IMAGE_MSG).status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/product/images/{productId}")
    public void serveProductImage(@PathVariable Long productId, HttpServletResponse response) throws IOException {
        log.info("Initiated request for serve image details with  productId:{}",productId );
        ProductDto productDto = productService.getSingleProduct(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        log.info("Request completed for serve image details with  productId:{}",productId );
    }
}
