package com.lcwt.electronic.store.repositories;

import com.lcwt.electronic.store.entities.Cart;
import com.lcwt.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);
}
