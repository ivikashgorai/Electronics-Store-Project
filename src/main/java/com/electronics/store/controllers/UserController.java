package com.electronics.store.controllers;

import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.dtos.response_message.ImageResponseMessage;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.services.Interfaces.FileServiceInterface;
import com.electronics.store.services.Interfaces.UserServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Tag(name = "User Management", description = "Operations related to user management")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private FileServiceInterface fileServiceInterface;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    @Operation(summary = "Create a new User", description = "Endpoint to create a new user")
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody(description = "User object to create", content = @Content(schema = @Schema(implementation = UserDto.class))) UserDto userDto
    ) {
        UserDto user = userServiceInterface.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing User", description = "Update user details by userId")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody(description = "User object to update", content = @Content(schema = @Schema(implementation = UserDto.class))) UserDto userDto,
            @PathVariable("userId") String userId
    ) {
        UserDto user = userServiceInterface.updateUser(userDto, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Delete a User", description = "Delete user by userId")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String id) throws IOException {
        userServiceInterface.deleteUser(id);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User is successfully deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Get all Users", description = "Get paginated list of all users")
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "2") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        return new ResponseEntity<>(userServiceInterface.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @Operation(summary = "Get User by ID", description = "Get a user by its userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String id) {
        return new ResponseEntity<>(userServiceInterface.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get User by Email", description = "Get a user by its email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userServiceInterface.getUserByEmail(email), HttpStatus.OK);
    }

    @Operation(summary = "Search Users by keyword", description = "Search users by name or email keyword")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable("keyword") String keyword) {
        return new ResponseEntity<>(userServiceInterface.getUserByKeyword(keyword), HttpStatus.OK);
    }

    @Operation(summary = "Upload User Profile Image", description = "Upload a profile image for a user")
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponseMessage> uploadUserImage(
            @RequestParam("userImage") MultipartFile userImage,
            @PathVariable("userId") String userId
    ) throws IOException {
        String imageName = fileServiceInterface.uploadFile(userImage, imageUploadPath);
        UserDto userDto = userServiceInterface.getUserById(userId);
        userDto.setImageName(imageName);
        UserDto updatedUserDto = userServiceInterface.updateUser(userDto, userId);
        ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponseMessage, HttpStatus.CREATED);
    }

    @Operation(summary = "Serve User Profile Image", description = "Get the profile image of a user")
    @GetMapping("/image/{userId}")
    public void serveImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
        UserDto userDto = userServiceInterface.getUserById(userId);
        InputStream resource = fileServiceInterface.getResource(imageUploadPath, userDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
