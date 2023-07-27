package com.lcwt.electronic.store.controller;

import com.lcwt.electronic.store.dtos.ImageResponse;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.servicesI.FileService;
import com.lcwt.electronic.store.servicesI.UserServiceI;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceI userServiceI;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;
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
        return new ResponseEntity<>(userDto1,HttpStatus.OK);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getSingleUserById(@PathVariable Long userId){
        log.info("Request entering for getSingle user with userId:{}",userId);
        UserDto userById = userServiceI.getUserById(userId);
        log.info("Request completed for getSingle user with userId:{}",userId);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(@RequestParam (value = "pageSize",defaultValue = "3",required = false) Integer pageSize,
                                                       @RequestParam (value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                       @RequestParam (value = "sortBy", defaultValue = "userName",required = false) String sortBy,
                                                       @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir  ){
        log.info("Request for getAllUser");
        PageableResponse<UserDto> allUser = this.userServiceI.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        log.info("Request completed for getAllUser");
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId){
        log.info("request entering for delete user with userId:{}",userId);
        this.userServiceI.deleteUser(userId);
        log.info("request completed for delete user with userId:{}",userId);
        return new ResponseEntity<String>(AppConstants.USER_DELETE, HttpStatus.OK);
    }
    @GetMapping("/userrs/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email){
        log.info("request for getUser with email:{}",email);
        User userByemail = this.userServiceI.getUserByemail(email);
        log.info("request completed for getUser with email:{}",email);
        return new ResponseEntity<User>(userByemail,HttpStatus.OK);
    }

    @GetMapping("/user/{keyword}")
    public ResponseEntity<List<User>> searchUser(@PathVariable("keyword") String keyword){
        log.info("request for searchUser with keyword:{}",keyword);
        List<User> users = this.userServiceI.searchUser(keyword);
        log.info("request completed for searchUser with keyword:{}",keyword);
        return new ResponseEntity<List<User>>(users,HttpStatus.FOUND);
    }
    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestPart("userImage" ) MultipartFile image,
            @PathVariable Long userId
            ) throws IOException {
        log.info("Initiated request for upload User Image with  userId:{}",userId);
        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userServiceI.getUserById(userId);
        user.setImage_name(imageName);
        UserDto userDto = userServiceI.updateUser(user, userId);
        ImageResponse response = ImageResponse.builder().imageName(imageName).status(HttpStatus.CREATED).build();
        log.info("Completed request for upload user image with  userId:{}",userId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    //serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable Long userId,HttpServletResponse response) throws IOException {
        log.info("Initiated request for serve image details with  userId:{}",userId);
        UserDto user = userServiceI.getUserById(userId);
        log.info("User Image name:{}",user.getImage_name());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImage_name());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        log.info("Completed request for serve image details with  userId:{}",userId);

    }
}
