package com.electronics.store;

import com.electronics.store.entities.Role;
import com.electronics.store.entities.User;
import com.electronics.store.entities.UserRole;
import com.electronics.store.entities.UserRoleId;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.RoleRepository;
import com.electronics.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ElectronicsStoreProjectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsStoreProjectApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		boolean admin = roleRepository.findByName("ROLE_ADMIN").isPresent();
		boolean normal = roleRepository.findByName("ROLE_NORMAL").isPresent();

		if (!admin) {
			Role role = new Role();
			role.setId(UUID.randomUUID().toString());
			role.setName("ROLE_ADMIN");
			roleRepository.save(role);
		}

		if (!normal) {
			Role role = new Role();
			role.setId(UUID.randomUUID().toString());
			role.setName("ROLE_NORMAL");
			roleRepository.save(role);
		}

		boolean adminUser = userRepository.findByEmail("vikashwork321@gmail.com").isPresent();
		if (!adminUser) {
			User user = new User();
			user.setName("Vikash");
			user.setEmail("vikashwork321@gmail.com"); // username is email here
			user.setGender("M");
			user.setImageName("vikash.png");
			user.setPassword(passwordEncoder.encode("Vikash"));
			user.setAbout("This is admin user");
			user.setUserId(UUID.randomUUID().toString());

			Role adminRole = roleRepository.findByName("ROLE_ADMIN")
					.orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

			// Create UserRole link
			UserRole userRoleLink = UserRole.builder()
					.id(new UserRoleId(user.getUserId(), adminRole.getId()))
					.user(user)
					.role(adminRole)
					.build();

			user.getUserRoles().add(userRoleLink);

			userRepository.save(user);
		}
	}

}
