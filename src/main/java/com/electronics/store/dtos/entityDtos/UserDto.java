package com.electronics.store.dtos.entityDtos;

import com.electronics.store.entities.Role;
import com.electronics.store.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    // all code copied from User Entity,leave code which you don't want to expose
    private String userId;

    //Api Validation
    @Size(min  = 3,max = 20,message = "Invalid Name") // if condition not followed then this message as Exception
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "Invalid User email")
    private String email;


    @Pattern(regexp = "^[MF]$",message = "gender must be M or F")
    private String gender;

    @NotBlank(message = "About can't be blank")
    private String about;

    @ImageNameValid
    private String imageName;

    private List<RoleDto> roles = new ArrayList<>();

}
