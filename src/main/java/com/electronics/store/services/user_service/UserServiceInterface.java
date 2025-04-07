package com.electronics.store.services.user_service;

import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.entityDtos.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserServiceInterface {

    //create
//    User createUser(User user);  don't use User here,Use DTO , check notes
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId) throws IOException;

    //getAllUser
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //getUserById
    UserDto getUserById(String userId);

    //getUserByEmail
    UserDto getUserByEmail(String email);

    //getUserBySearchKeyword
    List<UserDto> getUserByKeyword(String keyword);

}
