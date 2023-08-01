package com.lcwt.electronic.store.controller;

import com.lcwt.electronic.store.dtos.CategoryDto;
import com.lcwt.electronic.store.dtos.ImageResponse;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.ProductDto;
import com.lcwt.electronic.store.exceptions.ApiResponseMessage;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.servicesI.CategoryService;
import com.lcwt.electronic.store.servicesI.FileService;
import com.lcwt.electronic.store.servicesI.ProductService;
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
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private static Logger log= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService cateService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.image.path}")
    private String imagePath;
    /**
     * @author DipaliC
     * @apiNote Methods for Category Controller
     * @param
     * @return
     */
    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        log.info("Initiated request for create category details...");
        CategoryDto category = this.cateService.createCategory(categoryDto);
        log.info("Request Completed for create category...");
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long categoryId){
        log.info("Initiated request for update category with categoryId : {}",categoryId);
        CategoryDto updaetCategory = this.cateService.updaetCategory(categoryDto, categoryId);
        log.info("Request completed for update category with categoryId :{}",categoryId);
        return new ResponseEntity<>(updaetCategory,HttpStatus.CREATED);
    }
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable Long categoryId){
        log.info("Requesting for delete category with categoryId :{}",categoryId);
        this.cateService.deleteCategory(categoryId);
        ApiResponseMessage apiResponseMessage= ApiResponseMessage.builder().message(AppConstants.CATEGORY_DELETE).status(HttpStatus.OK).success(true).build();
        log.info("Request completed for delete category with categoryId:{}",categoryId);
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId){
        log.info("Initiated request for getCategory with categoryId:{}",categoryId);
        CategoryDto category = this.cateService.getCategory(categoryId);
        log.info("Request Completed for getCategory with categoryId:{}",categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/category")
    public ResponseEntity<PageableResponse<CategoryDto>> getCategories(
            @RequestParam (value = "pageSize",defaultValue = "3",required = false) Integer pageSize,
            @RequestParam (value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam (value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        log.info("Initiated request for getCategories..");
        PageableResponse<CategoryDto> categories = this.cateService.getCategories(pageNumber, pageSize, sortBy, sortDir);
        log.info("request completed for getCategories...");
        return new ResponseEntity<PageableResponse<CategoryDto>>(categories,HttpStatus.OK);
    }

    //UploadImage
    @PostMapping("/category/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @PathVariable Long categoryId,
            @RequestPart(AppConstants.REQUEST_KEY) MultipartFile image
    ) throws IOException {

        String fileName = fileService.uploadFile(image, imagePath);
        CategoryDto categoryDto = cateService.getCategory(categoryId);
        categoryDto.setCoverImage(fileName);
         cateService.updaetCategory(categoryDto, categoryId);
        ImageResponse response = ImageResponse.builder().imageName(categoryDto.getCoverImage()).message(AppConstants.IMAGE_MSG).status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //ServeImage
    @GetMapping("/category/images/{categoryId}")
    public void serveImage(@PathVariable Long categoryId, HttpServletResponse response) throws IOException {
        log.info("Initiated request for serve image details with  categoryId:{}",categoryId );
        CategoryDto categoryDto = cateService.getCategory(categoryId);
        InputStream resource = fileService.getResource(imagePath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        log.info("Request completed for serve image details with  categoryId:{}",categoryId );
    }

    //product with category
    @PostMapping("/category/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
                    @PathVariable("categoryId") Long categoryId,
                    @RequestBody ProductDto productDTo
                    ){

        ProductDto productWithCategory = productService.createWithCategory(productDTo, categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);

    }

}
