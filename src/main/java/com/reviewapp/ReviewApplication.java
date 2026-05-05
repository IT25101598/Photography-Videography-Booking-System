package com.reviewapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * ReviewApplication - Spring Boot entry point.
 * @SpringBootApplication enables component-scan on com.reviewapp
 */
@SpringBootApplication
public class ReviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewApplication.class, args);
    }
}