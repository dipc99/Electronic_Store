package com.lcwt.electronic.store.repositories;

import com.lcwt.electronic.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
}
