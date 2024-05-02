package com.microservices.authenticationservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		String username = dotenv.get("POSTGRES_USER");
		String password = dotenv.get("POSTGRES_PASSWORD");
		String dbName = dotenv.get("POSTGRES_DB");
		String url = dotenv.get("POSTGRES_URL");

		if (username == null || password == null || dbName == null || url == null) {
			System.err.println("Critical environment variables are missing!");
			if (username == null) System.err.println("Missing: POSTGRES_USER");
			if (password == null) System.err.println("Missing: POSTGRES_PASSWORD");
			if (dbName == null) System.err.println("Missing: POSTGRES_DB");
			if (url == null) System.err.println("Missing: SPRING_DATASOURCE_URL");
			System.exit(1);
		}

		System.setProperty("spring.datasource.username", username);
		System.setProperty("spring.datasource.password", password);
		System.setProperty("spring.datasource.url", url);

		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
