package com.UserManagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication request payload")
public class AuthRequest {

    @NotBlank
    @Schema(description = "Username for login", required = true, example = "john_doe")
    private String username;

    @NotBlank
    @Schema(description = "Password for authentication", required = true, example = "password")
    private String password;
}
