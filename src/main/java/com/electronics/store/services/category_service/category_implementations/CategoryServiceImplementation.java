package com.electronics.store.services.category_service.category_implementations;

import com.electronics.store.dtos.entityDtos.CategoryDto;
import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.entities.Category;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.CategoryRepository;
import com.electronics.store.services.category_service.CategoryServiceInterface;
import com.electronics.store.utility.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class CategoryServiceImplementation implements CategoryServiceInterface {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.cover.image.path}")
    private String imagePath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        categoryDto.setCategoryId(UUID.randomUUID().toString());
        Category category = mapper.map(categoryDto,Category.class);
        Category saved = categoryRepository.save(category);
        return mapper.map(saved,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category updatedcategory = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found to update"));
        updatedcategory.setDescription(categoryDto.getDescription());
        updatedcategory.setTitle(categoryDto.getTitle());
        updatedcategory.setCoverImage(categoryDto.getCoverImage());
        categoryRepository.save(updatedcategory);
        return mapper.map(updatedcategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
       Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category with "+categoryId+" not found"));

        String imageName = category.getCoverImage();
        String fullImagePath = imagePath+ File.separator+imageName;
        try { //  below deleting image of category
            // sometimes it give file cant be access error
            // but when we will upload in server this error will be gone
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        }
        catch (Exception e){ // if file does not exist
            System.out.println(e.getMessage()+" No Such File exist");
        }

       categoryRepository.deleteById(categoryId);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {

        //sorting
        Sort sort = sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();


        //Pagination
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> pagableResponse = Helper.getPagableResponse(page, CategoryDto.class);

        return pagableResponse;

    }

    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        return mapper.map(categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category id does not exist")),CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategoryByTitle(String keyword) {
        List<Category> list = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Category c : list){
            categoryDtos.add(mapper.map(c,CategoryDto.class));
        }
        return categoryDtos;
    }
}
