package com.lcwt.electronic.store.controller;

import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.servicesI.UserServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private static Logger log= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceI userServiceI;
    /**
     * @author DipaliC
     * @apiNote Methods For User Controller
     * @param userDto
     * @return
     */
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        log.info("Request Entering for createUser");
        UserDto user = this.userServiceI.createUser(userDto);
        log.info("Request Completed of CreateUser...");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto>  updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long userId){
        log.info("Request entering for update user with userId:{}",userId);
        UserDto userDto1 = this.userServiceI.updateUser(userDto, userId);
        log.info("request completed for update user with userId:{}",userId);
        return new ResponseEntity<>(userDto1,HttpStatus.CREATED);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getSingleUserById(@PathVariable Long userId){
        log.info("Request entering for getSingle user with userId:{}",userId);
        UserDto userById = userServiceI.getUserById(userId);
        log.info("Request completed for getSingle user with userId:{}",userId);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUser(@RequestParam (value = "pageSize",defaultValue = "5",required = false) Integer pageSize,
                                                                                @RequestParam (value = "pageNumber", defaultValue = "0") Integer pageNumber){
        log.info("Request for getAllUser");
        List<UserDto> allUser = this.userServiceI.getAllUser(pageNumber,pageSize);
        log.info("Request completed for getAllUser");
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        log.info("request entering for delete user with userId:{}",userId);
        this.userServiceI.deleteUser(userId);
        log.info("request completed for delete user with userId:{}",userId);
        return new ResponseEntity<String>(AppConstants.USER_DELETE, HttpStatus.OK);
    }
    @GetMapping("/users/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        log.info("request for getUser with email:{}",email);
        UserDto userByemail = this.userServiceI.getUserByemail(email);
        log.info("request completed for getUser with email:{}",email);
        return new ResponseEntity<>(userByemail,HttpStatus.OK);
    }

    @GetMapping("/users/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword){
        log.info("request for searchUser with keyword:{}",keyword);
        List<UserDto> searchUser = this.userServiceI.searchUser(keyword);
        log.info("request completed for searchUser with keyword:{}",keyword);
        return new ResponseEntity<List<UserDto>>(searchUser,HttpStatus.OK);
    }
}
