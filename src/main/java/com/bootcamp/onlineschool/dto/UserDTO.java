package com.bootcamp.onlineschool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "User data transfer object representing base user information")
public class UserDTO {
    private Long id; // Only used for responses, not for creation

    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Schema(description = "Timestamp when the user was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-01-01T10:00:00")
    private LocalDateTime updatedAt;

    // Default constructor
    public UserDTO() {}

    // Constructor with required fields
    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Constructor with all fields
    public UserDTO(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility method to convert from Entity to DTO
    public static UserDTO fromEntity(com.bootcamp.onlineschool.entity.User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    // Note: toEntity() method is not provided for UserDTO since User is abstract.
    // Use StudentDTO.toEntity() or TeacherDTO.toEntity() for concrete implementations.

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}