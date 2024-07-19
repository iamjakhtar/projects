package com.jdev.projects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectsApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ProjectsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=====================");
		System.out.println("Project is running...");
		System.out.println("=====================");
	}

}
