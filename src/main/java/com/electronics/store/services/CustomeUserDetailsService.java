package com.electronics.store.services;

import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username is email in this project
        return userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
}
