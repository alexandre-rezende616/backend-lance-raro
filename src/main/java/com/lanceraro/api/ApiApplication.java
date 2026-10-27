package com.lanceraro.api;

// Requires the Spring Boot dependency set in the project build (for example, Maven/Gradle starter dependencies).
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
