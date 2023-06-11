package com.lcwt.electronic.store.servicesI;

import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;

import java.util.List;

public interface UserServiceI {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto,Long userId);

    void deleteUser(Long userId);

    PageableResponse<UserDto> getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    //get single user by id
    UserDto getUserById(Long userId);
    //get single user by email
    User getUserByemail(String email);
    //SearchUser by keyword
    List<User> searchUser(String keyword);

}