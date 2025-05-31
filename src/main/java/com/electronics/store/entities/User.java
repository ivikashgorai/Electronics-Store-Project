package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


//using lombok for getter setter and constructor
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//above is using lombok, in runtime auto. all things will get created
@Entity
@Table(name = "users")
public class User implements UserDetails {
    //can use @Column(name ="") for column name etc.
     @Id //primary key
    private String userId;
     @Column(name="user_name",length = 20)
    private String name;
    @Column(name="user_email",unique = true)
    private String email;
    @Column(name="user_password",length = 1000)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name="user_image_name") // profile image name
    private String imageName;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)//user delete ho toh order delete ho jaye
    private List<Order> order;


    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<UserRole> userRoles = new ArrayList<>();

    // convenience: return roles directly
    public List<Role> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .toList();
    }



    @Override // yaha role jayega
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        return simpleGrantedAuthorities;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    //get password method is already there with @getter @setter

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
