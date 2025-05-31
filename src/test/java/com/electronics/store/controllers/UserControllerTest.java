package com.electronics.store.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.entities.Role;
import com.electronics.store.entities.User;
import com.electronics.store.services.Interfaces.UserServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceInterface userServiceInterface;

    @Autowired
    private ModelMapper mapper;

    private User user;
    private Role role;

    @BeforeEach
    public void init() {
        role = Role.builder()
                .id("abc")
                .name("ROLE_ADMIN")
                .build();

        user = User.builder()
                .name("Vikash")
                .email("vikash@gmail.com")
                .about("This is testing user")
                .gender("M")
                .imageName("abc.png")
                .password("vikash")
                .roles(List.of(role))
                .build();
    }

    @Test
    public void createUserTest() throws Exception {
        Mockito.when(userServiceInterface.createUser(Mockito.any()))
                .thenReturn(mapper.map(user, UserDto.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    private String convertObjectToJson(User user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}