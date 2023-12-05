package com.example.movieticketingsystem;

import com.example.movieticketingsystem.entity.Role;
import com.example.movieticketingsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
@RequiredArgsConstructor
public class MovieTicketingSystemApplication implements CommandLineRunner {

	private final RoleRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(MovieTicketingSystemApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		if(!repository.findByName("USER").isPresent()){
			Role role = new Role();
			role.setActive(true);
//			role.setCreatedBy("hussainmuzamil199@gmail.com");

			role.setName("USER");
			repository.save(role);
		}
		if(!repository.findByName("ADMIN").isPresent()){
			Role role = new Role();
			role.setActive(true);
//			role.setCreatedBy("hussainmuzamil199@gmail.com");
			role.setName("ADMIN");
			repository.save(role);
		}
	}
}
