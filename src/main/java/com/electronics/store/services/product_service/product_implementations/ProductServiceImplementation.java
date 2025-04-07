package com.electronics.store.services.product_service.product_implementations;

import com.electronics.store.dtos.entityDtos.ProductDto;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.entities.Product;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.ProductRepository;
import com.electronics.store.services.product_service.ProductServiceInterface;
import com.electronics.store.utility.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@Primary
public class ProductServiceImplementation implements ProductServiceInterface {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        Product product = mapper.map(productDto,Product.class);
        Product saved = productRepository.save(product);
        return mapper.map(saved,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product toBeUpdatedProduct  = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
        toBeUpdatedProduct.setTitle(productDto.getTitle());
        toBeUpdatedProduct.setDescription(productDto.getDescription());
        toBeUpdatedProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        toBeUpdatedProduct.setPrice(productDto.getPrice());
        toBeUpdatedProduct.setQuantity(productDto.getQuantity());
        toBeUpdatedProduct.setLive(productDto.isLive());
        toBeUpdatedProduct.setStock(productDto.isStock());
        Product save = productRepository.save(toBeUpdatedProduct);
        return mapper.map(save,ProductDto.class);

    }

    @Override
    public void deleteProduct(String productId) {
        Product product  = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
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
}
