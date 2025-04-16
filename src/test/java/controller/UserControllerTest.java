package controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private User testUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("testuser2")
                .email("test2@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(now)
                .updatedAt(now)
                .build();

        userList = Arrays.asList(testUser, user2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @WithMockUser
    void getUserById_ShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = testUser;
        updatedUser.setEmail("updated@example.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
