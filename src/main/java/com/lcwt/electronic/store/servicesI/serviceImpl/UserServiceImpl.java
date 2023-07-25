package com.lcwt.electronic.store.servicesI.serviceImpl;

import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;
import com.lcwt.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.helper.Helper;
import com.lcwt.electronic.store.repositories.UserRepo;
import com.lcwt.electronic.store.servicesI.UserServiceI;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServiceI {

    private static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * @author DipaliC.
     * @implNote Methods for Users Service implementation
     * @param userDto
     * @return
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in Long format
//        Long userId= Long.valueOf(UUID.randomUUID().toString());
//        userDto.setUserId(userId);
        User user = this.modelMapper.map(userDto, User.class);
        log.info("Initiating the DAO call for createUser");
        User user1=this.userRepo.save(user);
        log.info("completed DAO call of createUser...");
        return this.modelMapper.map(user1,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Entering DAO call for updating User with userId:{} ",userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USER_ID, userId));

      //  user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setImage_name(userDto.getImage_name());

        User user1 = this.userRepo.save(user);
        UserDto userDto1 = this.modelMapper.map(user1, UserDto.class);
        log.info("Completed DAO call for updateUser with userId:{}",userId);
        return userDto1;

    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Entering DAO call for delete user with userId:{}",userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USER_ID, userId));
        this.userRepo.deleteById(userId);
        log.info("completed DAO call for deleteUser using userId:{}",userId);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("EnteringDAO call for getAllUser");
        Sort sort = (sortDir.equalsIgnoreCase(AppConstants.SORT_DESC))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()) ;
        Pageable p= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> findAll = this.userRepo.findAll(p);
        log.info("completed DAO call for getAllUser");
        PageableResponse<UserDto> response = Helper.getPageableResponse(findAll, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(Long userId)
    {
        log.info("Entering DAO call for find single user by userId:{}",userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USER_ID, userId));
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        log.info("Completed DAO call for get single user with userId :{}",userId);
        return userDto;
    }

    @Override
    public User getUserByemail(String email) {
        log.info("Entering DAO call for get user with email:{}",email);
        User user = this.userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstants.NOT_FOUND));
        //UserDto userDto = this.modelMapper.map(user, UserDto.class);
        log.info("completed DAO call for find user with email:{}",email);
        return  user;
    }

    @Override
    public List<User> searchUser(String keyword) {
        log.info("Entering DAO call for search user with keyword:{}",keyword);
        List<User> users = this.userRepo.findByUserNameContaining(keyword);
        //List<UserDto> userDtos = users.stream().map((user) -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("Completed DAO call for search user with keyword:{}",keyword);
        return users;
    }
}