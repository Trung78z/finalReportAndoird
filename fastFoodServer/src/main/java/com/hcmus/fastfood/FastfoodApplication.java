package com.hcmus.fastfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMethodSecurity
@RestController
public class FastfoodApplication {
	@GetMapping("/hello")
	public String getGreeting() {
		return "Hello, World!";
	}
	public static void main(String[] args) {
		SpringApplication.run(FastfoodApplication.class, args);
	}

}
