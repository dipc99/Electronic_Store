package com.lcwt.electronic.store.repositories;

import com.lcwt.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {


}
