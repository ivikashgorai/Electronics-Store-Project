package com.electronics.store.services.product_service;


import com.electronics.store.dtos.entityDtos.ProductDto;
import com.electronics.store.dtos.paging_response.PageableResponse;

import java.util.List;

public interface ProductServiceInterface {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto,String productId);

    //delete
    void deleteProduct(String productId);

    //get single
    ProductDto getSingleProduct(String productId);

    //get all
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize,String sortBy,String sortDir);

    //get all : live
    PageableResponse<ProductDto> getAllLiveProduct(int pageNumber, int pageSize,String sortBy,String sortDir);

    //search product
    PageableResponse<ProductDto> getSearchedByTitleProduct(String title,int pageNumber, int pageSize,String sortBy,String sortDir);

    // create product with category
    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);

    //assign category to existing product
    ProductDto assignCategoryToProduct(String productId,String categoryId);

    //get products of a category
    List<ProductDto> getAllProductsOfACategory(String categoryId);
}
