package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.ProductDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.dtos.response_message.ImageResponseMessage;
import com.electronics.store.services.Interfaces.FileServiceInterface;
import com.electronics.store.services.Interfaces.ProductServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@Tag(name = "Product Management", description = "Operations related to product management")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceInterface productServiceInterface;

    @Autowired
    private FileServiceInterface fileServiceInterface;

    @Value("${product.image.path}")
    private String imageUploadPath;

    // Create Product
    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Requires JWT token",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    public ResponseEntity<ProductDto> createProduct(
            @Valid
            @RequestBody ProductDto productDto
    ) {
        ProductDto product = productServiceInterface.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // Update Product
    @PutMapping("/{productId}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid
            @RequestBody ProductDto productDto,
            @Parameter(description = "Product ID to update", in = ParameterIn.PATH)
            @PathVariable("productId") String categoryId
    ){
        ProductDto product = productServiceInterface.updateProduct(productDto,categoryId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<ApiResponseMessage> deleteProduct(
            @Parameter(description = "Product ID to delete", in = ParameterIn.PATH)
            @PathVariable("productId") String productId
    ){
        productServiceInterface.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponseMessage("Product deleted successfully",true,HttpStatus.OK),HttpStatus.OK);
    }

    // Get single Product
    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<ProductDto> getProduct(
            @Parameter(description = "Product ID to retrieve", in = ParameterIn.PATH)
            @PathVariable("productId") String productId
    ){
        ProductDto product = productServiceInterface.getSingleProduct(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    // Get all Products with pagination
    @GetMapping
    @Operation(summary = "Get all products with pagination")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @Parameter(description = "Page number", in = ParameterIn.QUERY)
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @Parameter(description = "Page size", in = ParameterIn.QUERY)
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @Parameter(description = "Sort by field", in = ParameterIn.QUERY)
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", in = ParameterIn.QUERY)
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse =  productServiceInterface.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    // Get all live products
    @GetMapping("/live")
    @Operation(summary = "Get all live products with pagination")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(
            @Parameter(description = "Page number", in = ParameterIn.QUERY)
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @Parameter(description = "Page size", in = ParameterIn.QUERY)
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @Parameter(description = "Sort by field", in = ParameterIn.QUERY)
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", in = ParameterIn.QUERY)
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse =  productServiceInterface.getAllLiveProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    // Search products by title
    @GetMapping("/search/{title}")
    @Operation(summary = "Search products by title with pagination")
    public ResponseEntity<PageableResponse<ProductDto>> getProductByTitle(
            @Parameter(description = "Title keyword to search", in = ParameterIn.PATH)
            @PathVariable("title") String title,
            @Parameter(description = "Page number", in = ParameterIn.QUERY)
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @Parameter(description = "Page size", in = ParameterIn.QUERY)
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @Parameter(description = "Sort by field", in = ParameterIn.QUERY)
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", in = ParameterIn.QUERY)
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse =  productServiceInterface.getSearchedByTitleProduct(title,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    // Upload Product Image
    @PostMapping("/image/{productId}")
    @Operation(summary = "Upload product image")
    public ResponseEntity<ImageResponseMessage> uploadProductImage(
            @Parameter(description = "Image file to upload", in = ParameterIn.QUERY)
            @RequestParam("productImage") MultipartFile productImage,
            @Parameter(description = "Product ID to upload image for", in = ParameterIn.PATH)
            @PathVariable("productId") String productId
    ) throws IOException {
        String imageName = fileServiceInterface.uploadFile(productImage, imageUploadPath);
        ProductDto productDto = productServiceInterface.getSingleProduct(productId);
        productDto.setProductImageName(imageName);
        ProductDto updatedProductDto = productServiceInterface.updateProduct(productDto, productId);
        ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder()
                .message("Image Uploaded")
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponseMessage,HttpStatus.CREATED);
    }

    // Serve Product Image
    @GetMapping("/image/{productId}")
    @Operation(summary = "Serve product image")
    public void serveCategoryCoverImage(
            @Parameter(description = "Product ID to serve image", in = ParameterIn.PATH)
            @PathVariable("productId") String productId,
            HttpServletResponse response
    ) throws IOException {
        ProductDto productDto = productServiceInterface.getSingleProduct(productId);
        InputStream resource = fileServiceInterface.getResource(imageUploadPath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
