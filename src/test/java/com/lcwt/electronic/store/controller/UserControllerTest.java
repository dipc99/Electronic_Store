package com.lcwt.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwt.electronic.store.dtos.PageableResponse;
import com.lcwt.electronic.store.dtos.UserDto;
import com.lcwt.electronic.store.entities.User;
import com.lcwt.electronic.store.servicesI.UserServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    // Testing Using  MockMvc Framework
    @MockBean
    private UserServiceI userServiceI;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MockMvc mockMvc;

   private User user;
    @BeforeEach
    void setUp(){
        user = User.builder().about("testing for controller")
                .userName("Dipali")
                .email("dipC99@gmail.com")
                .gender("female")
                .password("Dipali")
                .image_name("xyz.png")
                .build();
    }
    @Test
    void createUserTest() throws Exception {
        UserDto userDto = this.mapper.map(user, UserDto.class);
        Mockito.when(userServiceI.createUser(Mockito.any())).thenReturn(userDto);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.userName").exists());
    }
    @Test
    public void updateUserTest() throws Exception {
        //users/{userId}+PUT request + json
        Long userId=11L;
        UserDto userDto = this.mapper.map(user, UserDto.class);
        Mockito.when(userServiceI.updateUser(Mockito.any(),Mockito.anyLong())).thenReturn(userDto);
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users/"+userId)
                     .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonString(user))
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.userName").exists());
    }
                //method for conversion
    private String convertObjectToJsonString(Object user) {

        try{
            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void getAll() throws Exception {

        UserDto object1 = UserDto.builder()
                .userName("Jay")
                .email("jay@gmail.com")
                .image_name("jjj.png")
                .password("Jay111")
                .about("Teacher")
                .gender("Female").build();
        UserDto object2 = UserDto.builder()
                .userName("Disha")
                .email("d@gmail.com")
                .image_name("ddd.png")
                .password("Disha111")
                .about("Doctor")
                .gender("Female")
                .build();
        UserDto object3 = UserDto.builder()
                .userName("swati")
                .email("s@gmail.com")
                .image_name("sss.png")
                .password("Swati111")
                .about("Tester")
                .gender("Female")
                .build();

        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(object1,object2,object3));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElement(1000);

        Mockito.when(userServiceI.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getSingleUserTest() throws Exception {
        Long userId=12L;
        UserDto userDto = this.mapper.map(user, UserDto.class);
        Mockito.when(userServiceI.getUserById(Mockito.anyLong())).thenReturn(userDto);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void deleteUserTest() throws Exception {
        Long userId=12L;
        UserDto userDto = this.mapper.map(user, UserDto.class);
        Mockito.doNothing().when(userServiceI).deleteUser(Mockito.anyLong());
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/users/"+userId))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getUserByEmailTest() throws Exception {
        String emailId="Dipali@gmail.com";

        Mockito.when(userServiceI.getUserByemail(Mockito.anyString())).thenReturn(user);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/userrs/"+emailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void searchUserTest() throws Exception {
        String keyword="name";
        User user1 = User.builder()
                .userName("Jay")
                .email("jay@gmail.com")
                .image_name("jjj.png")
                .password("Jay111")
                .about("Teacher")
                .gender("Female").build();
        User user2 = User.builder()
                .userName("Disha")
                .email("d@gmail.com")
                .image_name("ddd.png")
                .password("Disha111")
                .about("Doctor")
                .gender("Female")
                .build();

        Mockito.when(userServiceI.searchUser(Mockito.anyString())).thenReturn(Arrays.asList(user1,user2));
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/"+keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound());

    }

}
