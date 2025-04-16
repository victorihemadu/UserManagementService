package com.UserManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "User entity representing system users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true)
    @Schema(description = "Username for login", required = true, example = "john_doe")
    private String username;

    @JsonIgnore
    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(description = "Password for authentication (not returned in responses)", required = true)
    private String password;

    @NotBlank
    @Email
    @Column(unique = true)
    @Schema(description = "Email address", required = true, example = "john.doe@example.com")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "User role for authorization", required = true, example = "USER")
    private Role role;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the user was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Schema(description = "Timestamp when the user was last updated")
    private LocalDateTime updatedAt;
}
