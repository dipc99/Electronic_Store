package com.lcwt.electronic.store.servicesI;

import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;

import java.util.List;

public interface UserServiceI {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto,Long userId);

    void deleteUser(Long userId);

    List<UserDto> getAllUser(Integer pageNumber, Integer pageSize);
    //get single user by id
    UserDto getUserById(Long userId);
    //get single user by email
    UserDto getUserByemail(String email);
    //SearchUser by keyword
    List<UserDto> searchUser(String keyword);

}