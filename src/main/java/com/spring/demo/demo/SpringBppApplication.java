package com.spring.demo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBppApplication.class, args);
	}
}
