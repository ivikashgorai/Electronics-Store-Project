package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.ProductDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.dtos.response_message.ImageResponseMessage;
import com.electronics.store.services.Interfaces.FileServiceInterface;
import com.electronics.store.services.Interfaces.ProductServiceInterface;
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

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceInterface productServiceInterface;

    @Autowired
    private FileServiceInterface fileServiceInterface;

    @Value("${product.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid
            @RequestBody ProductDto productDto
    ) {
        ProductDto product = productServiceInterface.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid
            @RequestBody ProductDto productDto,
            @PathVariable("productId") String categoryId
    ){
        ProductDto product = productServiceInterface.updateProduct(productDto,categoryId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(
            @PathVariable("productId") String productId
    ){
        productServiceInterface.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponseMessage("Product deleted successfully",true,HttpStatus.OK),HttpStatus.OK);
    }

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") String productId){
        ProductDto product = productServiceInterface.getSingleProduct(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
    @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
    @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
    @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
    @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse =  productServiceInterface.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse =  productServiceInterface.getAllLiveProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //get by title
    @GetMapping("/search/{title}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductByTitle(
            @PathVariable("title") String title,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse =  productServiceInterface.getSearchedByTitleProduct(title,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponseMessage> uploadProductImage(
            @RequestParam("productImage") MultipartFile productImage,
            @PathVariable("productId") String productId
    ) throws IOException {
        String imageName = fileServiceInterface.uploadFile(productImage, imageUploadPath);
        ProductDto productDto = productServiceInterface.getSingleProduct(productId);
        productDto.setProductImageName(imageName);
        ProductDto updatedProductDto = productServiceInterface.updateProduct(productDto, productId);
        ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().message("Image Uploaded").imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponseMessage,HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{productId}")
    public void serveCategoryCoverImage(
            @PathVariable("productId") String productId,
            HttpServletResponse response
    ) throws IOException {
        ProductDto productDto = productServiceInterface.getSingleProduct(productId);
        InputStream resource = fileServiceInterface.getResource(imageUploadPath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE); //output file type as it is an image
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
