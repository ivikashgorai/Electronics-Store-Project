package com.electronics.store.service;

import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.entities.Role;
import com.electronics.store.entities.User;
import com.electronics.store.repositories.RoleRepository;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.services.Interfaces.UserServiceInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest // for context of spring boot like Autowired
public class UserServiceTest {

    @Autowired
    private ModelMapper mapper;

    @MockitoBean // dont use mock when using @Autowired which come in spring context )in dependent service
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    //    @InjectMocks //injecting the mock as we dont want to use real repositories
    @Autowired // this uses userRepo. and roleRepo so we dont want it actually so we we will mock it
    private UserServiceInterface userServiceInterface;

    User user;
    Role role;
    String roleId;

    @BeforeEach
    public void init(){
        role= Role.builder()
                .id("abc")
                .name("ROLE_ADMIN")
                .build();

        user =  User.builder()
                .name("Vikash")
                .email("vikash@gmail.com")
                .about("This is testing user")
                .gender("M")
                .imageName("abc.png")
                .password("vikash")
                .roles(List.of(role))
                .build();

        roleId = "abc";
    }

    @Test
    public void createUserTest(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user); //Mockito.any refers to passing any thing
        Mockito.when(roleRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(role));

        UserDto userDto = userServiceInterface.createUser(mapper.map(user, UserDto.class));
        System.out.println(userDto.getName());
        Assertions.assertNotNull(userDto);

    }
}