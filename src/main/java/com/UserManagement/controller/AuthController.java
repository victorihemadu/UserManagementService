package com.UserManagement.controller;

import com.UserManagement.model.AuthRequest;
import com.UserManagement.model.User;
import com.UserManagement.security.JwtTokenProvider;
import com.UserManagement.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthRequest request) {
        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            User user = userService.findByUsername(request.getUsername());

            // Generate token
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());

            // Return token
            Map<String, String> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("token", token);
            response.put("role", user.getRole().name());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
