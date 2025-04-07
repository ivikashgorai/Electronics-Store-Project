package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;


//using lombok for getter setter and constructor
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//above is using lombok , in runtime auto. all things will get created
@Entity
@Table(name = "users")
public class User {
    //can use @Column(name ="") for column name etc.
     @Id //primary key
    private String userId;
     @Column(name="user_name",length = 20)
    private String name;
    @Column(name="user_email",unique = true)
    private String email;
    @Column(name="user_password",length = 10)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name="user_image_name") // profile image name
    private String imageName;


}
