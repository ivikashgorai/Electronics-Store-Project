package com.electronics.store.services.user_service.user_interface;

import com.electronics.store.dtos.UserDto;

import java.util.List;

public interface UserServiceInterface {

    //create
//    User createUser(User user);  don't use User here,Use DTO , check notes
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    //getAllUser
    List<UserDto> getAllUser();

    //getUserById
    UserDto getUserById(String userId);

    //getUserByEmail
    UserDto getUserByEmail(String email);

    //getUserBySearchKeyword
    List<UserDto> getUserByKeyword(String keyword);

}
