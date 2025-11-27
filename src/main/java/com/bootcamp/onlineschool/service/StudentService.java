package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.StudentRepository;
import com.bootcamp.onlineschool.repository.ClazzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Student entities
 * Provides CRUD operations, relationship management, and business logic validation
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClazzRepository clazzRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, ClazzRepository clazzRepository) {
        this.studentRepository = studentRepository;
        this.clazzRepository = clazzRepository;
    }

    /**
     * Create a new student
     * @param studentDTO the student data
     * @return the created student
     * @throws ValidationException if validation fails
     */
    public StudentDTO create(StudentDTO studentDTO) {
        validateStudentForCreation(studentDTO);

        Student student = studentDTO.toEntity();
        // Auto-generate studentId
        String nextStudentId = generateNextStudentId();
        student.setStudentId(nextStudentId);

        Student savedStudent = studentRepository.save(student);
        return StudentDTO.fromEntity(savedStudent);
    }

    /**
     * Retrieve all students
     * @return List of all students as DTOs
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream()
                .map(StudentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find student by ID
     * @param id the student ID
     * @return the student DTO if found
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public StudentDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return StudentDTO.fromEntity(student);
    }

    /**
     * Find student by student ID
     * @param studentId the student ID
     * @return Optional containing the student DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<StudentDTO> findByStudentId(String studentId) {
        validateStudentId(studentId);
        return studentRepository.findByStudentId(studentId)
                .map(StudentDTO::fromEntity);
    }

    /**
     * Find student by email
     * @param email the email address
     * @return Optional containing the student DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<StudentDTO> findByEmail(String email) {
        validateEmail(email);
        return studentRepository.findByEmail(email)
                .map(StudentDTO::fromEntity);
    }

    /**
     * Find students by class ID
     * @param clazzId the class ID
     * @return List of students enrolled in the class
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findStudentsByClazzId(Long clazzId) {
        if (clazzId == null) {
            throw new ValidationException("Class ID cannot be null");
        }
        return studentRepository.findStudentsByClazzId(clazzId).stream()
                .map(StudentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing student
     * @param id the student ID
     * @param studentDTO the updated student data
     * @return the updated student DTO
     * @throws ResourceNotFoundException if student not found
     * @throws ValidationException if validation fails
     */
    public StudentDTO update(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        validateStudentForUpdate(studentDTO, existingStudent);
        
        // Update fields
        existingStudent.setName(studentDTO.getName());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setStudentId(studentDTO.getStudentId());
        existingStudent.setEnrollmentDate(studentDTO.getEnrollmentDate());
        
        Student savedStudent = studentRepository.save(existingStudent);
        return StudentDTO.fromEntity(savedStudent);
    }

    /**
     * Delete student by ID
     * @param id the student ID
     * @throws ResourceNotFoundException if student not found
     */
    public void deleteById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        studentRepository.delete(student);
    }

    /**
     * Enroll student in a class
     * @param studentId the student ID
     * @param clazzId the class ID
     * @return the updated student DTO
     * @throws ResourceNotFoundException if student or class not found
     * @throws ValidationException if enrollment validation fails
     */
    public StudentDTO enrollInClass(Long studentId, Long clazzId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        validateClassEnrollment(student, clazz);
        
        student.addClazz(clazz);
        Student savedStudent = studentRepository.save(student);
        return StudentDTO.fromEntity(savedStudent);
    }

    /**
     * Remove student from a class
     * @param studentId the student ID
     * @param clazzId the class ID
     * @return the updated student DTO
     * @throws ResourceNotFoundException if student or class not found
     */
    public StudentDTO removeFromClass(Long studentId, Long clazzId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        student.removeClazz(clazz);
        Student savedStudent = studentRepository.save(student);
        return StudentDTO.fromEntity(savedStudent);
    }

    /**
     * Check if student exists by student ID
     * @param studentId the student ID
     * @return true if student exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByStudentId(String studentId) {
        validateStudentId(studentId);
        return studentRepository.existsByStudentId(studentId);
    }

    // Private validation methods

    private void validateStudentForCreation(StudentDTO studentDTO) {
        if (studentDTO == null) {
            throw new ValidationException("Student data cannot be null");
        }

        validateRequiredFields(studentDTO);

        // Check for duplicate student ID
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new ValidationException("Student ID already exists: " + studentDTO.getStudentId());
        }

        // Check for duplicate email
        if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            throw new ValidationException("Email already exists: " + studentDTO.getEmail());
        }
    }

    private void validateStudentForUpdate(StudentDTO studentDTO, Student existingStudent) {
        if (studentDTO == null) {
            throw new ValidationException("Student data cannot be null");
        }

        validateRequiredFields(studentDTO);

        // Check if student ID is being changed to an existing one
        if (!existingStudent.getStudentId().equals(studentDTO.getStudentId())) {
            if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
                throw new ValidationException("Student ID already exists: " + studentDTO.getStudentId());
            }
        }

        // Check if email is being changed to an existing one
        if (!existingStudent.getEmail().equals(studentDTO.getEmail())) {
            if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
                throw new ValidationException("Email already exists: " + studentDTO.getEmail());
            }
        }
    }

    private void validateRequiredFields(StudentDTO studentDTO) {
        if (studentDTO.getName() == null || studentDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }

        if (studentDTO.getEmail() == null || studentDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        // Remove studentId check
        if (studentDTO.getEnrollmentDate() == null) {
            throw new ValidationException("Enrollment date is required");
        }

        if (studentDTO.getEnrollmentDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Enrollment date cannot be in the future");
        }
    }

    private void validateStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be null or empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
    }

    private void validateClassEnrollment(Student student, Clazz clazz) {
        // Check if student is already enrolled in the class
        if (student.getClasses().contains(clazz)) {
            throw new ValidationException("Student is already enrolled in class: " + clazz.getName());
        }

        // Check class capacity
        if (clazz.getStudents().size() >= clazz.getMaxCapacity()) {
            throw new ValidationException("Class has reached maximum capacity: " + clazz.getMaxCapacity());
        }
    }

    // Add this helper method to StudentService
    private String generateNextStudentId() {
        long count = studentRepository.count() + 1;
        String id;
        do {
            id = String.format("STU%04d", count++);
        } while (studentRepository.existsByStudentId(id));
        return id;
    }
}