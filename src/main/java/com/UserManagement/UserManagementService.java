package com.UserManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(
        title = "User Management API",
        version = "1.0",
        description = "User Management Service with Spring Boot"
))

public class UserManagementService {
    public static void main(String[] args) {
        SpringApplication.run(UserManagementService.class, args);
    }
    }
