package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.UserDTO;
import com.bootcamp.onlineschool.entity.User;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing User entities
 * Provides CRUD operations and business logic validation
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user
     * Note: This creates a base User entity. For Students and Teachers, use their respective services.
     * @param userDTO the user data
     * @return the created user DTO
     * @throws ValidationException if validation fails
     */
    public UserDTO create(UserDTO userDTO) {
        validateUserForCreation(userDTO);
        
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        User savedUser = userRepository.save(user); // id is auto-generated
        return UserDTO.fromEntity(savedUser);
    }

    /**
     * Retrieve all users
     * @return List of all users as DTOs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find user by ID
     * @param id the user ID
     * @return the user DTO if found
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserDTO.fromEntity(user);
    }

    /**
     * Find user by email
     * @param email the email address
     * @return Optional containing the user DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        validateEmail(email);
        return userRepository.findByEmail(email)
                .map(UserDTO::fromEntity);
    }

    /**
     * Check if user exists by email
     * @param email the email address
     * @return true if user exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        validateEmail(email);
        return userRepository.existsByEmail(email);
    }

    /**
     * Update an existing user
     * @param id the user ID
     * @param userDTO the updated user data
     * @return the updated user DTO
     * @throws ResourceNotFoundException if user not found
     * @throws ValidationException if validation fails
     */
    public UserDTO update(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Validate business rules
        validateUserForUpdate(userDTO, existingUser);
        
        // Update fields
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        
        User savedUser = userRepository.save(existingUser);
        return UserDTO.fromEntity(savedUser);
    }

    /**
     * Delete user by ID
     * @param id the user ID
     * @throws ResourceNotFoundException if user not found
     */
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    /**
     * Validate email format and non-null
     * @param email the email to validate
     * @throws ValidationException if email is invalid
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
    }

    /**
     * Validate user data for creation operations
     * @param userDTO the user data to validate
     * @throws ValidationException if validation fails
     */
    private void validateUserForCreation(UserDTO userDTO) {
        if (userDTO == null) {
            throw new ValidationException("User data cannot be null");
        }

        // Validate required fields
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ValidationException("Email already exists: " + userDTO.getEmail());
        }
    }

    /**
     * Validate user data for update operations
     * @param userDTO the user data to validate
     * @param existingUser the existing user
     * @throws ValidationException if validation fails
     */
    private void validateUserForUpdate(UserDTO userDTO, User existingUser) {
        if (userDTO == null) {
            throw new ValidationException("Updated user data cannot be null");
        }

        // Check if email is being changed to an existing email
        if (!existingUser.getEmail().equals(userDTO.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new ValidationException("Email already exists: " + userDTO.getEmail());
            }
        }

        // Validate required fields
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
    }
}