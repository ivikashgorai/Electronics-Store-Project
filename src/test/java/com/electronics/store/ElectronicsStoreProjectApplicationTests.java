package com.electronics.store;

import com.electronics.store.entities.Role;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.security.JwtHelper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

@SpringBootTest
class ElectronicsStoreProjectApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}

	@Test
	void testToken(){
		User user = userRepository.findByEmail("vikashwork321@gmail.com").orElseThrow(()-> new ResourceNotFoundException("User not found"));
		String token = jwtHelper.generateToken(user, user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()).toString());
		Claims allClaimsFromToken = jwtHelper.getAllClaimsFromToken(token);
		System.out.println(token+"\n"+allClaimsFromToken);
		System.out.println(jwtHelper.getUsernameFromToken(token));
	}

}
