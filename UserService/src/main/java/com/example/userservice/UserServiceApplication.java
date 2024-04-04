package com.example.userservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.example.Models")
public class UserServiceApplication {

	public static void main(String[] args){
		SpringApplication.run(UserServiceApplication.class, args);
	}
	@Bean
	public PasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}



}
