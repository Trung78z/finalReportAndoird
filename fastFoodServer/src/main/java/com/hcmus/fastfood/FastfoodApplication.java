package com.hcmus.fastfood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMethodSecurity
@RestController
public class FastfoodApplication {
	static Logger logger = LoggerFactory.getLogger(FastfoodApplication.class);

	@GetMapping("/hello")
	public String getGreeting() {
		return "Hello, World!";
	}

	public static void main(String[] args) {
		logInfo();
		SpringApplication.run(FastfoodApplication.class, args);
	}

	public static void logInfo() {
		logger.trace("A TRACE Message");
		logger.debug("A DEBUG Message");
		logger.info("An INFO Message");
		logger.warn("A WARN Message");
		logger.error("An ERROR Message");
	}

}
