package com.example.booklending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.booklending")
public class BookLendingApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(BookLendingApplication.class);

		// Uncomment the line below for local development
        app.setAdditionalProfiles("local");
		app.run(args);
	}

}
