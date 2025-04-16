package service;

import com.UserManagement.exceptions.ResourceNotFoundException;
import com.UserManagement.model.Role;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

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
    }

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        // Arrange
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User savedUser = userService.createUser(testUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserWhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.getUserById(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user2 = User.builder()
                .id(2L)
                .username("testuser2")
                .email("test2@example.com")
                .password("password2")
                .role(Role.USER)
                .build();

        List<User> userList = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> foundUsers = userService.getAllUsers();

        // Assert
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser_ShouldUpdateUserDetailsAndSave() {
        // Arrange
        User updatedUser = User.builder()
                .id(1L)
                .username("updated")
                .email("updated@example.com")
                .password("newpassword")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encoded_newpassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getRole(), result.getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteExistingUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void findByUsername_ShouldReturnUserWhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.findByUsername("testuser");

        // Assert
        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void findByUsername_ShouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername("nonexistent"));
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}
