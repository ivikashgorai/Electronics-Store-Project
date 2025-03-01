package com.electronics.store.services.user_service.user_implementations;

import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.User;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.services.user_service.user_interface.UserServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class UserServiceImplementation implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
//        userRepository.save(userDto); gives error as repo want User not UserDto, so we have to convert from dto to entity and then entity to dto
        //get random Id
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Give User Id not found"));
        user.setImageName(userDto.getImageName());
        user.setName(userDto.getName());
        user.setGender(userDto.getGender());
//        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        UserDto updatedDto = entityToDto(user);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException(""));
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> allUser = userRepository.findAll();
        List<UserDto> allUserDto = new ArrayList<>();
        for(User u:allUser){
            allUserDto.add(entityToDto(u));
        }
        return allUserDto;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Id not found"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Email id not found"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> getUserByKeyword(String keyword) {
        List<User> user = userRepository.findByNameContaining(keyword);
        List<UserDto> userDto = new ArrayList<>();
        for(User u : user){
            userDto.add(entityToDto(u));
        }
        return userDto;
    }


    private UserDto entityToDto(User savedUser) { // we use library to avoid below code
//        UserDto userDto = new UserDto();
//        userDto.setUserId(savedUser.getUserId());
//        userDto.setName(savedUser.getName());
//        userDto.setGender(savedUser.getGender());
//        userDto.setEmail(savedUser.getEmail());
//        userDto.setAbout(savedUser.getAbout());
//        userDto.setPassword(savedUser.getPassword());
//        userDto.setImageName(savedUser.getImageName());
//        return userDto;

        //below Using Model mapper
        return modelMapper.map(savedUser,UserDto.class); // start,destination
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = new User();
//        user.setUserId(userDto.getUserId());
//        user.setName(userDto.getName());
//        user.setGender(userDto.getGender());
//        user.setEmail(userDto.getEmail());
//        user.setAbout(userDto.getAbout());
//        user.setPassword(userDto.getPassword());
//        user.setImageName(userDto.getImageName());
//        return user;
        return modelMapper.map(userDto,User.class);
    }
}
