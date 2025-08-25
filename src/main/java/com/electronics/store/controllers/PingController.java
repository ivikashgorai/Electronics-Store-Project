package com.electronics.store.controllers;

import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.security.JwtHelper;
import com.electronics.store.services.Interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    private ResponseEntity<ApiResponseMessage> getUserProfileForPing(
    ){
        return ResponseEntity.ok(new ApiResponseMessage("Ping success",true, HttpStatus.ACCEPTED));
    }
}
