package com.lcwt.electronic.store.repositories;

import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    Optional<UserDto> findByEmail(String email);

    List<UserDto> findByUserNameContaining(String keyword);
}
