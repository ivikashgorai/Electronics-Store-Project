package com.electronics.store.controllers;

import com.electronics.store.dtos.ApiResponseMessage;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.services.user_service.user_interface.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @PostMapping //create
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){ //@valid for validation of data coming
      UserDto user =  userServiceInterface.createUser(userDto);
      return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody UserDto userDto,
            @PathVariable("userId") String userId
    ){
        UserDto user =  userServiceInterface.updateUser(userDto,userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(
            @PathVariable("userId") String id
    ){
        userServiceInterface.deleteUser(id);
        //instead of String we should send json
//        return new ResponseEntity<>("User is successfully deleted",HttpStatus.OK);
//        ApiResponseMessage message  = new ApiResponseMessage();
//        message.setMessage("User is successfully deleted");
//        message.setSuccess(true);
//        message.setStatus(HttpStatus.OK);
        //can do like above but use builder for easy
        ApiResponseMessage message = ApiResponseMessage.builder().message("User is successfully deleted").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    //get all
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser(){
        return new ResponseEntity<>(userServiceInterface.getAllUser(),HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable("userId") String id
    ){
        return new ResponseEntity<>(userServiceInterface.getUserById(id),HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable("email") String email
    ){
        return new ResponseEntity<>(userServiceInterface.getUserByEmail(email),HttpStatus.OK);
    }

    //get by keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(
            @PathVariable("keyword") String keyword
    ){
        return new ResponseEntity<>(userServiceInterface.getUserByKeyword(keyword),HttpStatus.OK);
    }

}
