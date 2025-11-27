package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Student data transfer object extending user information with student-specific details")
public class StudentDTO extends UserDTO {

    @Schema(description = "Unique student identifier", example = "STU001")
    private String studentId; // Not required for creation

    @Schema(description = "Date when the student enrolled", example = "2023-01-15", required = true)
    @NotNull(message = "Enrollment date is required")
    @Past(message = "Enrollment date must be in the past")
    private LocalDate enrollmentDate;

    @Schema(description = "List of class IDs the student is enrolled in", example = "[1, 2, 3]")
    private Set<Long> classIds = new HashSet<>();
    
    @Schema(description = "List of registration IDs for the student", example = "[1, 2, 3]")
    private Set<Long> registrationIds = new HashSet<>();

    // Default constructor
    public StudentDTO() {
        super();
    }

    // Constructor with required fields
    public StudentDTO(String name, String email, LocalDate enrollmentDate) {
        super(name, email);
        this.enrollmentDate = enrollmentDate;
    }

    // Constructor with all fields
    public StudentDTO(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt,
                     String studentId, LocalDate enrollmentDate) {
        super(id, name, email, createdAt, updatedAt);
        this.studentId = studentId;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Set<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(Set<Long> classIds) {
        this.classIds = classIds;
    }

    public Set<Long> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(Set<Long> registrationIds) {
        this.registrationIds = registrationIds;
    }

    // Utility method to convert from Entity to DTO
    public static StudentDTO fromEntity(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO(
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getCreatedAt(),
            student.getUpdatedAt(),
            student.getStudentId(),
            student.getEnrollmentDate()
        );

        // Convert class relationships to IDs
        if (student.getClasses() != null) {
            student.getClasses().forEach(clazz -> dto.getClassIds().add(clazz.getId()));
        }

        // Convert registration relationships to IDs
        if (student.getRegistrations() != null) {
            student.getRegistrations().forEach(registration -> dto.getRegistrationIds().add(registration.getId()));
        }

        return dto;
    }

    // Utility method to convert from DTO to Entity (for updates)
    public Student toEntity() {
        Student student = new Student();
        student.setId(this.getId());
        student.setName(this.getName());
        student.setEmail(this.getEmail());
        student.setStudentId(this.studentId);
        student.setEnrollmentDate(this.enrollmentDate);
        student.setCreatedAt(this.getCreatedAt());
        student.setUpdatedAt(this.getUpdatedAt());
        return student;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", classIds=" + classIds +
                ", registrationIds=" + registrationIds +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}