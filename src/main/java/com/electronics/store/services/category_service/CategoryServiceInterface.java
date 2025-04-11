package com.electronics.store.services.category_service;

import com.electronics.store.dtos.entityDtos.CategoryDto;
import com.electronics.store.dtos.paging_response.PageableResponse;

import java.util.List;

public interface CategoryServiceInterface {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category detail
    CategoryDto getSingleCategory(String categoryId);

    //search
    List<CategoryDto> getCategoryByTitle(String keyword);

}
