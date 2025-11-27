package com.bootcamp.onlineschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @NotBlank(message = "Employee ID is required")
    @Size(min = 3, max = 20, message = "Employee ID must be between 3 and 20 characters")
    @Column(name = "employee_id", unique = true, length = 20)
    private String employeeId; // Now nullable and auto-generated

    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 100, message = "Department must be between 2 and 100 characters")
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @NotNull(message = "Hire date is required")
    @Past(message = "Hire date must be in the past")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Clazz> classes = new HashSet<>();

    // Default constructor
    public Teacher() {
        super();
    }

    // Constructor with required fields (remove employeeId param)
    public Teacher(String name, String email, String department, LocalDate hireDate) {
        super(name, email);
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

    public Set<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(Set<Clazz> classes) {
        this.classes = classes;
    }

    // Helper methods for managing relationships
    public void addClazz(Clazz clazz) {
        classes.add(clazz);
        clazz.setTeacher(this);
    }

    public void removeClazz(Clazz clazz) {
        classes.remove(clazz);
        clazz.setTeacher(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        // If both have IDs, use parent equals (which compares IDs)
        if (getId() != null && teacher.getId() != null) {
            return super.equals(o);
        }
        // If no IDs, compare by employeeId
        return employeeId != null && employeeId.equals(teacher.employeeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", hireDate=" + hireDate +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}