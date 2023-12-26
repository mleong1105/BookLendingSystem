package com.example.booklending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BookLendingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookLendingApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                configurer.setDefaultTimeout(999999); // Set the timeout in milliseconds
                configurer.registerCallableInterceptors(new TimeoutCallableProcessingInterceptor());
            }
        };
    }
}
