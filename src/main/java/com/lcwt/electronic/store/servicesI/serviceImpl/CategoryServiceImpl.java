package com.lcwt.electronic.store.servicesI.serviceImpl;

import com.lcwt.electronic.store.dtos.CategoryDto;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.entities.Category;
import com.lcwt.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.helper.Helper;
import com.lcwt.electronic.store.repositories.CategoryRepo;
import com.lcwt.electronic.store.servicesI.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static Logger log = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param categoryDto
     * @return
     * @author DipaliC.
     * @implNote Methods for CategoryServiceImpl
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("requesting DAO call for CreateCategory : ");
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category save = categoryRepo.save(category);
        CategoryDto cateDto = this.modelMapper.map(save, CategoryDto.class);
        log.info("request completed DAO call for createCategory  ");
        return cateDto;
    }

    @Override
    public CategoryDto updaetCategory(CategoryDto categoryDto, Long categoryId) {
        log.info("Initiated request dao call for update category with categoryId :{} ", categoryId);
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY, AppConstants.CATEGORY_ID, categoryId));

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category category2 = categoryRepo.save(category);
        log.info("request completed dao call for update category with categoryId :{} ", categoryId);
        return modelMapper.map(category2, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("requesting dao call for deleteCategory with categoryId :{} ", categoryId);
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY, AppConstants.CATEGORY_ID, categoryId));
        categoryRepo.delete(category);
        log.info("request completed dao call for deleteCategory with categoryId :{} ", categoryId);

    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        log.info("requesting dao call for getCategory with categoryId : {}", categoryId);
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY, AppConstants.CATEGORY_ID, categoryId));
        log.info("request completed dao call for getCategory with categoryId :{} ", categoryId);
        return modelMapper.map(category, CategoryDto.class);

    }

    @Override
    public PageableResponse<CategoryDto> getCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
        log.info("Request starting for dao layer to get all categories ");
        Sort sort = (sortDir.equalsIgnoreCase(AppConstants.SORT_TITLE)) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = this.categoryRepo.findAll(pageable);
        log.info("Request completed for dao layer to get All categories ");
        return Helper.getPageableResponse(categoryPage, CategoryDto.class);

    }
}
