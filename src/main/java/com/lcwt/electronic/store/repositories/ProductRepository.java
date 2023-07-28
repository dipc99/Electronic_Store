package com.lcwt.electronic.store.repositories;

import com.lcwt.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByTitleContaining(String title, Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);
}