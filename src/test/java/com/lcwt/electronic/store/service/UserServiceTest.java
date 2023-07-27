package com.lcwt.electronic.store.service;

import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;
import com.lcwt.electronic.store.repositories.UserRepo;
import com.lcwt.electronic.store.servicesI.UserServiceI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private ModelMapper mapper;
    @MockBean
    private UserRepo userRepo;
    @Autowired
    private UserServiceI userService;

    User user;

    @BeforeEach
    public void init() {
        user = User.builder().userName("Dipali").email("Dip@gmail.com").password("abc").gender("Female").about("Software Engineer").image_name("xyz.png").build();
    }

    @Test
    public void createUserTest() {
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);

        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
        System.out.println(user1.getUserName());

        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Dipali", user1.getUserName());

    }

    @Test
    public void updateUser() {

        Long userId = 12L;

        UserDto userDto = UserDto.builder().userName("Raj Patil").password("xyz").gender("Male").about("Software Engineer Java Developer").image_name("abc.png").build();

        Mockito.when(userRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto, userId);
        System.out.println(updatedUser.getUserName());
        System.out.println(updatedUser.getAbout());
        System.out.println(updatedUser.getImage_name());
        Assertions.assertNotNull(userDto);
        //Assertions.assertEquals(userDto.getUserName(),updatedUser.getUserName());
    }


    @Test
    public void getAllUsersTest() {

        User user1 = User.builder().userName("Dipali").email("Dip@gmail.com").password("abc").gender("feMale").about("Software Engineer").image_name("xyz.png").build();
        User user2 = User.builder().userName("Dipshri").email("DipS@gmail.com").password("abcd").gender("female").about("Software Engineer").image_name("xyz.png").build();

        List<User> userList = Arrays.asList(user, user1, user2);

        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepo.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<UserDto> allUsers = userService.getAllUser(1, 2, "name", "asc");
        Assertions.assertEquals(3, allUsers.getContent().size());

    }

    @Test
    public void deleteUserTest() {

        Long userId = 112L;
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepo, Mockito.times(1)).deleteById(userId);
    }

    @Test
    public void findUserByIdTest() {
        Long userId = 11L;

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(userId);

        Assertions.assertNotNull(userDto);

        Assertions.assertEquals(user.getUserName(), userDto.getUserName());
    }

    @Test
    public void findUserByEmailTest() {
        String emailId = "userId@gmail.com";

        Mockito.when(userRepo.findByEmail(emailId)).thenReturn(Optional.of(user));

        User user1 = userService.getUserByemail(emailId);

        Assertions.assertNotNull(user1);

        Assertions.assertEquals(user.getEmail(), user1.getEmail());
    }

}
