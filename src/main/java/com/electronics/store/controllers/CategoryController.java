package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.CategoryDto;
import com.electronics.store.dtos.entityDtos.ProductDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.dtos.response_message.ImageResponseMessage;
import com.electronics.store.services.Interfaces.CategoryServiceInterface;
import com.electronics.store.services.Interfaces.FileServiceInterface;
import com.electronics.store.services.Interfaces.ProductServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Tag(name = "Category Management", description = "Operations related to categories and category products")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceInterface categoryServiceInterface;

    @Autowired
    private ProductServiceInterface productServiceInterface;

    @Autowired
    private FileServiceInterface fileServiceInterface;

    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    @PostMapping
    @Operation(summary = "Create a new category", description = "Create a new category with validation")
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category details")
            @RequestBody CategoryDto categoryDto
    ){
        CategoryDto category = categoryServiceInterface.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category", description = "Update an existing category by ID")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @Parameter(description = "Category ID to update", in = ParameterIn.PATH)
            @PathVariable("categoryId") String categoryId
    ){
        CategoryDto category = categoryServiceInterface.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a category", description = "Delete a category by ID")
    public ResponseEntity<ApiResponseMessage> deleteCategory(
            @Parameter(description = "Category ID to delete", in = ParameterIn.PATH)
            @PathVariable("categoryId")  String categoryId
    ){
        categoryServiceInterface.deleteCategory(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Category with "+categoryId+ " is deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Fetch all categories with pagination and sorting")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue ="title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(categoryServiceInterface.getAllCategory(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a single category by ID")
    public ResponseEntity<CategoryDto> getSingleCategory(
            @Parameter(description = "Category ID to fetch", in = ParameterIn.PATH)
            @PathVariable("categoryId") String categoryId
    ){
        return new ResponseEntity<>(categoryServiceInterface.getSingleCategory(categoryId),HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    @Operation(summary = "Search categories by keyword")
    public ResponseEntity<List<CategoryDto>> getByKeyword(
            @Parameter(description = "Keyword to search categories", in = ParameterIn.PATH)
            @PathVariable("keyword") String keyword
    ){
        return  new ResponseEntity<>(categoryServiceInterface.getCategoryByTitle(keyword),HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    @Operation(summary = "Upload category cover image")
    public ResponseEntity<ImageResponseMessage> uploadCategoryCoverImage(
            @Parameter(description = "Cover image file") @RequestParam("categoryCoverImage") MultipartFile categoryCoverImage,
            @Parameter(description = "Category ID", in = ParameterIn.PATH) @PathVariable("categoryId") String categoryId
    ) throws IOException {
        String imageName = fileServiceInterface.uploadFile(categoryCoverImage, imageUploadPath);
        CategoryDto categoryDto = categoryServiceInterface.getSingleCategory(categoryId);
        categoryDto.setCoverImage(imageName);
        CategoryDto updatedCategoryDto = categoryServiceInterface.updateCategory(categoryDto, categoryId);
        ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponseMessage,HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    @Operation(summary = "Serve category cover image")
    public void serveCategoryCoverImage(
            @Parameter(description = "Category ID", in = ParameterIn.PATH) @PathVariable("categoryId") String categoryId,
            HttpServletResponse response
    ) throws IOException {
        CategoryDto categoryDto = categoryServiceInterface.getSingleCategory(categoryId);
        InputStream resource = fileServiceInterface.getResource(imageUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    @PostMapping("/{categoryId}/products")
    @Operation(summary = "Create a product under a category")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product details")
            @RequestBody ProductDto productDto,
            @Parameter(description = "Category ID", in = ParameterIn.PATH) @PathVariable("categoryId") String categoryId
    ){
        ProductDto productWithCategory = productServiceInterface.createProductWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    @PatchMapping("/{categoryId}/products/{productId}")
    @Operation(summary = "Assign category to product")
    public ResponseEntity<ProductDto> assignCategoryToProduct(
            @Parameter(description = "Product ID", in = ParameterIn.PATH) @PathVariable("productId") String productId,
            @Parameter(description = "Category ID", in = ParameterIn.PATH) @PathVariable("categoryId") String categoryId
    ){
        ProductDto dto = productServiceInterface.assignCategoryToProduct(productId, categoryId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    @Operation(summary = "Get all products of a category")
    public ResponseEntity<List<ProductDto>> getProductsOfACategory(
            @Parameter(description = "Category ID", in = ParameterIn.PATH) @PathVariable("categoryId") String categoryId
    ){
        List<ProductDto> allProductsOfACategory = productServiceInterface.getAllProductsOfACategory(categoryId);
        return new ResponseEntity<>(allProductsOfACategory,HttpStatus.OK);
    }
}
