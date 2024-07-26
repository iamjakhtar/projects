package com.jdev.projects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableWebSecurity
public class ProjectsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProjectsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=====================");
		System.out.println("Project is running...");
		System.out.println("=====================");
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
