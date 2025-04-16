package controller;

import com.UserManagement.controller.AuthController;
import com.example.usermanagement.model.AuthRequest;
import com.example.usermanagement.model.Role;
import com.example.usermanagement.model.User;
import com.example.usermanagement.security.JwtTokenProvider;
import com.example.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    private User testUser;
    private AuthRequest authRequest;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        authRequest = new AuthRequest("testuser", "password");

        authentication = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(jwtTokenProvider.createToken("testuser", "USER")).thenReturn("test-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).findByUsername("testuser");
        verify(jwtTokenProvider, times(1)).createToken("testuser", "USER");
    }
}

