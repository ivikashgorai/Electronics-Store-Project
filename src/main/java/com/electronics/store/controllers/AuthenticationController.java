package com.electronics.store.controllers;

import com.electronics.store.dtos.entityDtos.RefreshTokenDto;
import com.electronics.store.dtos.entityDtos.RoleDto;
import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.dtos.token_request_response.JwtRequest;
import com.electronics.store.dtos.token_request_response.JwtResponse;
import com.electronics.store.entities.Role;
import com.electronics.store.entities.User;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.security.JwtHelper;
import com.electronics.store.services.implementations.RefreshTokenImplementation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication and JWT tokens")
public class AuthenticationController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenImplementation refreshTokenImplementation;

    @PostMapping("/refresh-token/{token}")
    @Operation(summary = "Refresh JWT token", description = "Use a refresh token to generate a new JWT token")
    public ResponseEntity<JwtResponse> refreshToken(
            @Parameter(description = "Refresh token string", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable String token
    ){
        RefreshTokenDto refreshTokenDto = refreshTokenImplementation.findByToken(token);
        RefreshTokenDto refreshTokenDto1 = refreshTokenImplementation.verifyRefreshToken(refreshTokenDto);
        UserDto userDto = refreshTokenImplementation.getUser(refreshTokenDto1);

        String token1 = jwtHelper.generateToken(
                modelMapper.map(userDto, User.class),
                userDto.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet()).toString()
        );

        JwtResponse jwtResponse = JwtResponse.builder()
                .user(userDto)
                .refreshToken(refreshTokenDto)
                .token(token1).build();

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<JwtResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login credentials")
            @RequestBody JwtRequest jwtRequest
    ){
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        User user = (User) userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtHelper.generateToken(
                user,
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()).toString()
        );

        RefreshTokenDto refreshToken = refreshTokenImplementation.createRefreshToken(jwtRequest.getEmail());
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .user(modelMapper.map(user, UserDto.class))
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthenticate(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);
        } catch(BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
