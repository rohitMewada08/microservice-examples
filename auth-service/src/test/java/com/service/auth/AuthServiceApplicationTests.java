package com.service.auth;

import com.service.auth.dto.Role;
import com.service.auth.dto.User;
import com.service.auth.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AuthServiceApplicationTests {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

	@Test
	void createUser(){
		User user = new User();
		List<Role> roles = new ArrayList<>();

		user.setEmail("rohit_mewada@epam.com");
		user.setName("Rohit");
		user.setPassword(new BCryptPasswordEncoder().encode("rohit_mewada"));
		user.setPhoneNo("8770524131");
		Role role = new Role();
		role.setName("USER");
		role.setDescription("user role");
		roles.add(role);
		Role role1 = new Role();
		role1.setName("ADMIN");
		role1.setDescription("admin role");
		roles.add(role1);
		user.setRoles(roles);

		userRepository.save(user);
	}
}
