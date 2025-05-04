package com.electronics.store.services.implementations;

import com.electronics.store.dtos.entityDtos.ProductDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.entities.Category;
import com.electronics.store.entities.Product;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.CategoryRepository;
import com.electronics.store.repositories.ProductRepository;
import com.electronics.store.services.Interfaces.ProductServiceInterface;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class ProductServiceImplementation implements ProductServiceInterface {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.image.path}")
    private String imagePath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto,Product.class);
        Product saved = productRepository.save(product);
        return mapper.map(saved,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product toBeUpdatedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        //checking for null do that if did not send any variable it will remain same as it is
        //mtlb agr koi ek varibale ko bhi update karna chahe toh kar sakta hai
        if (productDto.getTitle() != null) {
            toBeUpdatedProduct.setTitle(productDto.getTitle());
        }

        if (productDto.getDescription() != null) {
            toBeUpdatedProduct.setDescription(productDto.getDescription());
        }

        if (productDto.getPrice() != 0) {
            toBeUpdatedProduct.setPrice(productDto.getPrice());
        }

        if (productDto.getDiscountedPrice() != 0) {
            toBeUpdatedProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        }

        if (productDto.getQuantity() != 0) {
            toBeUpdatedProduct.setQuantity(productDto.getQuantity());
        }

        if(productDto.getProductImageName()!=null){
            toBeUpdatedProduct.setProductImageName(productDto.getProductImageName());
        }
        // For boolean fields, consider using Boolean objects instead of primitive types
        toBeUpdatedProduct.setLive(productDto.isLive());
        toBeUpdatedProduct.setStock(productDto.isStock());

        Product save = productRepository.save(toBeUpdatedProduct);
        return mapper.map(save, ProductDto.class);
    }


    @Override
    public void deleteProduct(String productId) {
        Product product  = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
        String imageName = product.getProductImageName();
        String fullImagePath = imagePath + File.separator + imageName; //where image resides
        try{
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        }
        catch (Exception e){ // if file does not exist
            System.out.println(e.getMessage()+" No Such File exist");
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product  = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize,String sortBy,String sortDir) { //applying paging here
        Sort sort = sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPagableResponse(page,ProductDto.class);
        return pageableResponse;

    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPagableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getSearchedByTitleProduct(String title,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pagable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product>  page = productRepository.findByTitleContaining(title,pagable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPagableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto,String categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
        Product product = mapper.map(productDto,Product.class);
        product.setProductId(UUID.randomUUID().toString());
        product.setAddedDate(new Date());
        product.setCategory(category);
        System.out.println(product.getProductId());
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto assignCategoryToProduct(String productId, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        product.setCategory(category);
        Product save = productRepository.save(product);
        return mapper.map(save,ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProductsOfACategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        List<Product> products  = category.getProductList();
        return products.stream().map( product->
                mapper.map(product,ProductDto.class)
        ).toList();
    }


}
