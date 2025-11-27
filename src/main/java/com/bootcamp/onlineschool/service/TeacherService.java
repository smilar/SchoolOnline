package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.TeacherDTO;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import com.bootcamp.onlineschool.repository.ClazzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Teacher entities
 * Provides CRUD operations, class assignments, and business logic validation
 */
@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClazzRepository clazzRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, ClazzRepository clazzRepository) {
        this.teacherRepository = teacherRepository;
        this.clazzRepository = clazzRepository;
    }

    /**
     * Create a new teacher
     * @param teacherDTO the teacher data
     * @return the created teacher
     * @throws ValidationException if validation fails
     */
    public TeacherDTO create(TeacherDTO teacherDTO) {
        validateTeacherForCreation(teacherDTO);

        Teacher teacher = teacherDTO.toEntity();
        // Auto-generate employeeId
        String nextEmployeeId = generateNextEmployeeId();
        teacher.setEmployeeId(nextEmployeeId);

        Teacher savedTeacher = teacherRepository.save(teacher);
        return TeacherDTO.fromEntity(savedTeacher);
    }

    /**
     * Retrieve all teachers
     * @return List of all teachers as DTOs
     */
    @Transactional(readOnly = true)
    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll().stream()
                .map(TeacherDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find teacher by ID
     * @param id the teacher ID
     * @return the teacher DTO if found
     * @throws ResourceNotFoundException if teacher not found
     */
    @Transactional(readOnly = true)
    public TeacherDTO findById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return TeacherDTO.fromEntity(teacher);
    }

    /**
     * Find teacher by employee ID
     * @param employeeId the employee ID
     * @return Optional containing the teacher DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<TeacherDTO> findByEmployeeId(String employeeId) {
        validateEmployeeId(employeeId);
        return teacherRepository.findByEmployeeId(employeeId)
                .map(TeacherDTO::fromEntity);
    }

    /**
     * Find teacher by email
     * @param email the email address
     * @return Optional containing the teacher DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<TeacherDTO> findByEmail(String email) {
        validateEmail(email);
        return teacherRepository.findByEmail(email)
                .map(TeacherDTO::fromEntity);
    }

    /**
     * Find teachers by department
     * @param department the department name
     * @return List of teachers in the department
     */
    @Transactional(readOnly = true)
    public List<TeacherDTO> findByDepartment(String department) {
        validateDepartment(department);
        return teacherRepository.findTeachersByDepartment(department).stream()
                .map(TeacherDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find teachers by department (case-insensitive)
     * @param department the department name
     * @return List of teachers in the department
     */
    @Transactional(readOnly = true)
    public List<TeacherDTO> findByDepartmentIgnoreCase(String department) {
        validateDepartment(department);
        return teacherRepository.findTeachersByDepartmentIgnoreCase(department).stream()
                .map(TeacherDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing teacher
     * @param id the teacher ID
     * @param teacherDTO the updated teacher data
     * @return the updated teacher DTO
     * @throws ResourceNotFoundException if teacher not found
     * @throws ValidationException if validation fails
     */
    public TeacherDTO update(Long id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        
        validateTeacherForUpdate(teacherDTO, existingTeacher);
        
        // Update fields
        existingTeacher.setName(teacherDTO.getName());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setEmployeeId(teacherDTO.getEmployeeId());
        existingTeacher.setDepartment(teacherDTO.getDepartment());
        existingTeacher.setHireDate(teacherDTO.getHireDate());
        
        Teacher savedTeacher = teacherRepository.save(existingTeacher);
        return TeacherDTO.fromEntity(savedTeacher);
    }

    /**
     * Delete teacher by ID
     * @param id the teacher ID
     * @throws ResourceNotFoundException if teacher not found
     * @throws ValidationException if teacher has assigned classes
     */
    public void deleteById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        
        // Check if teacher has assigned classes
        if (!teacher.getClasses().isEmpty()) {
            throw new ValidationException("Cannot delete teacher with assigned classes. Please reassign classes first.");
        }
        
        teacherRepository.delete(teacher);
    }

    /**
     * Assign teacher to a class
     * @param teacherId the teacher ID
     * @param clazzId the class ID
     * @return the updated teacher DTO
     * @throws ResourceNotFoundException if teacher or class not found
     * @throws ValidationException if assignment validation fails
     */
    public TeacherDTO assignToClass(Long teacherId, Long clazzId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        validateClassAssignment(teacher, clazz);
        
        teacher.addClazz(clazz);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return TeacherDTO.fromEntity(savedTeacher);
    }

    /**
     * Remove teacher from a class
     * @param teacherId the teacher ID
     * @param clazzId the class ID
     * @return the updated teacher DTO
     * @throws ResourceNotFoundException if teacher or class not found
     */
    public TeacherDTO removeFromClass(Long teacherId, Long clazzId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        teacher.removeClazz(clazz);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return TeacherDTO.fromEntity(savedTeacher);
    }

    /**
     * Check if teacher exists by employee ID
     * @param employeeId the employee ID
     * @return true if teacher exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByEmployeeId(String employeeId) {
        validateEmployeeId(employeeId);
        return teacherRepository.existsByEmployeeId(employeeId);
    }

    // Private validation methods

    private void validateTeacherForCreation(TeacherDTO teacherDTO) {
        if (teacherDTO == null) {
            throw new ValidationException("Teacher data cannot be null");
        }

        validateRequiredFields(teacherDTO);

        // Check for duplicate employee ID
        if (teacherRepository.existsByEmployeeId(teacherDTO.getEmployeeId())) {
            throw new ValidationException("Employee ID already exists: " + teacherDTO.getEmployeeId());
        }

        // Check for duplicate email
        if (teacherRepository.findByEmail(teacherDTO.getEmail()).isPresent()) {
            throw new ValidationException("Email already exists: " + teacherDTO.getEmail());
        }
    }

    private void validateTeacherForUpdate(TeacherDTO teacherDTO, Teacher existingTeacher) {
        if (teacherDTO == null) {
            throw new ValidationException("Teacher data cannot be null");
        }

        validateRequiredFields(teacherDTO);

        // Check if employee ID is being changed to an existing one
        if (!existingTeacher.getEmployeeId().equals(teacherDTO.getEmployeeId())) {
            if (teacherRepository.existsByEmployeeId(teacherDTO.getEmployeeId())) {
                throw new ValidationException("Employee ID already exists: " + teacherDTO.getEmployeeId());
            }
        }

        // Check if email is being changed to an existing one
        if (!existingTeacher.getEmail().equals(teacherDTO.getEmail())) {
            if (teacherRepository.findByEmail(teacherDTO.getEmail()).isPresent()) {
                throw new ValidationException("Email already exists: " + teacherDTO.getEmail());
            }
        }
    }

    private void validateRequiredFields(TeacherDTO teacherDTO) {
        if (teacherDTO.getName() == null || teacherDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }

        if (teacherDTO.getEmail() == null || teacherDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        if (teacherDTO.getDepartment() == null || teacherDTO.getDepartment().trim().isEmpty()) {
            throw new ValidationException("Department is required");
        }

        if (teacherDTO.getHireDate() == null) {
            throw new ValidationException("Hire date is required");
        }

        if (teacherDTO.getHireDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Hire date cannot be in the future");
        }
    }

    private void validateEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new ValidationException("Employee ID cannot be null or empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
    }

    private void validateDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new ValidationException("Department cannot be null or empty");
        }
    }

    private void validateClassAssignment(Teacher teacher, Clazz clazz) {
        // Check if class already has a teacher assigned
        if (clazz.getTeacher() != null && !clazz.getTeacher().equals(teacher)) {
            throw new ValidationException("Class already has a teacher assigned: " + clazz.getTeacher().getName());
        }

        // Check if teacher is already assigned to this class
        if (teacher.getClasses().contains(clazz)) {
            throw new ValidationException("Teacher is already assigned to class: " + clazz.getName());
        }
    }

    // Add this helper method to TeacherService
    private String generateNextEmployeeId() {
        long count = teacherRepository.count() + 1;
        String id;
        do {
            id = String.format("EMP%04d", count++);
        } while (teacherRepository.existsByEmployeeId(id));
        return id;
    }
}