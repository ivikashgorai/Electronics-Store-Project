package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.CategoryDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.dtos.response_message.ImageResponseMessage;
import com.electronics.store.services.category_service.CategoryServiceInterface;
import com.electronics.store.services.file_service.FileServiceInterface;
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

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceInterface categoryServiceInterface;

    @Autowired
    private FileServiceInterface fileServiceInterface;

    @Value("${category.cover.image.path}") // from application.properties
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid // for api data validation
            @RequestBody
            CategoryDto categoryDto
    ){
       CategoryDto category = categoryServiceInterface.createCategory(categoryDto);
       return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid
            @RequestBody
            CategoryDto categoryDto,
            @PathVariable("categoryId") String categoryId
    ){
        CategoryDto category = categoryServiceInterface.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(
         @PathVariable("categoryId")  String categoryId
    ){
        categoryServiceInterface.deleteCategory(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Category with "+categoryId+ " is deleted").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue ="title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(categoryServiceInterface.getAllCategory(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(
            @PathVariable("categoryId") String categoryId
    ){
        return new ResponseEntity<>(categoryServiceInterface.getSingleCategory(categoryId),HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> getByKeyword(
            @PathVariable("keyword") String keyword
    ){
        return  new ResponseEntity<>(categoryServiceInterface.getCategoryByTitle(keyword),HttpStatus.OK);
    }

    //upload cover image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponseMessage> uploadCategoryCoverImage(
            @RequestParam("categoryCoverImage") MultipartFile categoryCoverImage,
            @PathVariable("categoryId") String categoryId
    ) throws IOException {
        String imageName = fileServiceInterface.uploadFile(categoryCoverImage, imageUploadPath);
        CategoryDto categoryDto = categoryServiceInterface.getSingleCategory(categoryId);
        categoryDto.setCoverImage(imageName);
        CategoryDto updatedCategoryDto = categoryServiceInterface.updateCategory(categoryDto, categoryId);
        ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponseMessage,HttpStatus.CREATED);
    }

    //serve cover image
    @GetMapping("/image/{categoryId}")
    public void serveCategoryCoverImage(
            @PathVariable("categoryId") String categoryId,
            HttpServletResponse response
    ) throws IOException {
        CategoryDto categoryDto = categoryServiceInterface.getSingleCategory(categoryId);
        InputStream resource = fileServiceInterface.getResource(imageUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE); //output file type as it is an image
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
