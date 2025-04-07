package com.electronics.store.repositories;

import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(String keyword,Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable); // when live is true
}
