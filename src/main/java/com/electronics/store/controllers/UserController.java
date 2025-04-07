package com.electronics.store.controllers;

import com.electronics.store.dtos.response_message.ApiResponseMessage;
import com.electronics.store.dtos.response_message.ImageResponseMessage;
import com.electronics.store.dtos.paging_response.PageableResponse;
import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.services.file_service.file_interface.FileServiceInterface;
import com.electronics.store.services.user_service.UserServiceInterface;
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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private FileServiceInterface fileServiceInterface;

    @Value("${user.profile.image.path}") // from application.properties
    private String imageUploadPath;

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
    ) throws IOException {
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
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            // applying pagination
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber, //page number index starts from 0
            @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy, // can be any variable of User like email,about etc.
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir // can be desc or asc
    ){
        return new ResponseEntity<>(userServiceInterface.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
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

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponseMessage> uploadUserImage(
            @RequestParam("userImage") MultipartFile userImage,
            @PathVariable("userId") String userId
    ) throws IOException {
        String imageName = fileServiceInterface.uploadFile(userImage, imageUploadPath);
        UserDto userDto  = userServiceInterface.getUserById(userId);
        userDto.setImageName(imageName);
        UserDto updatedUserDto = userServiceInterface.updateUser(userDto, userId);
        ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponseMessage,HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping("/image/{userId}")
    public void serveImage(
            @PathVariable("userId") String userId,
            HttpServletResponse response
    ) throws IOException {
        UserDto userDto = userServiceInterface.getUserById(userId);
        InputStream resource = fileServiceInterface.getResource(imageUploadPath, userDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE); //output file type as it is an image
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
