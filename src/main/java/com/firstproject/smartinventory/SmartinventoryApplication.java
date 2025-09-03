package com.firstproject.smartinventory;

import com.firstproject.smartinventory.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SmartinventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartinventoryApplication.class, args);


	}



}
