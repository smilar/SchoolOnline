package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.Teacher;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Teacher data transfer object extending user information with teacher-specific details")
public class TeacherDTO extends UserDTO {

    @Schema(description = "Unique employee identifier", example = "EMP001")
    private String employeeId; // Not required for creation

    @Schema(description = "Department where the teacher works", example = "Computer Science", required = true)
    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 100, message = "Department must be between 2 and 100 characters")
    private String department;

    @Schema(description = "Date when the teacher was hired", example = "2022-08-15", required = true)
    @NotNull(message = "Hire date is required")
    @Past(message = "Hire date must be in the past")
    private LocalDate hireDate;

    @Schema(description = "List of class IDs the teacher is assigned to", example = "[1, 2, 3]")
    private Set<Long> classIds = new HashSet<>();

    // Default constructor
    public TeacherDTO() {
        super();
    }

    // Constructor with required fields
    public TeacherDTO(String name, String email, String employeeId, String department, LocalDate hireDate) {
        super(name, email);
        this.employeeId = employeeId;
        this.department = department;
        this.hireDate = hireDate;
    }

    // Constructor for creation (no employeeId)
    public TeacherDTO(String name, String email, String department, LocalDate hireDate) {
        super(name, email);
        this.department = department;
        this.hireDate = hireDate;
    }

    // Constructor with all fields
    public TeacherDTO(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt,
                     String employeeId, String department, LocalDate hireDate) {
        super(id, name, email, createdAt, updatedAt);
        this.employeeId = employeeId;
        this.department = department;
        this.hireDate = hireDate;
    }

    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Set<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(Set<Long> classIds) {
        this.classIds = classIds;
    }

    // Utility method to convert from Entity to DTO
    public static TeacherDTO fromEntity(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        TeacherDTO dto = new TeacherDTO(
            teacher.getId(),
            teacher.getName(),
            teacher.getEmail(),
            teacher.getCreatedAt(),
            teacher.getUpdatedAt(),
            teacher.getEmployeeId(),
            teacher.getDepartment(),
            teacher.getHireDate()
        );

        // Convert class relationships to IDs
        if (teacher.getClasses() != null) {
            teacher.getClasses().forEach(clazz -> dto.getClassIds().add(clazz.getId()));
        }

        return dto;
    }

    // Utility method to convert from DTO to Entity (for updates)
    public Teacher toEntity() {
        Teacher teacher = new Teacher();
        teacher.setId(this.getId());
        teacher.setName(this.getName());
        teacher.setEmail(this.getEmail());
        teacher.setEmployeeId(this.employeeId);
        teacher.setDepartment(this.department);
        teacher.setHireDate(this.hireDate);
        teacher.setCreatedAt(this.getCreatedAt());
        teacher.setUpdatedAt(this.getUpdatedAt());
        return teacher;
    }

    @Override
    public String toString() {
        return "TeacherDTO{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", hireDate=" + hireDate +
                ", classIds=" + classIds +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}