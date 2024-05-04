package com.microservices.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

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

		SpringApplication.run(UserServiceApplication.class, args);
	}


	@Bean
	public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
								 @Value("${application-version}") String appVersion) {
		return new OpenAPI()
				.info(new Info()
						.title("User Service REST Endpoints")
						.version(appVersion)
						.description(appDesciption)
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

}
