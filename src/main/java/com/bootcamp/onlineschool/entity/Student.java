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
@Table(name = "students")
public class Student extends User {

    @NotBlank(message = "Student ID is required")
    @Size(min = 3, max = 20, message = "Student ID must be between 3 and 20 characters")
    @Column(name = "student_id", nullable = true, unique = true, length = 20)
    private String studentId; // Now nullable and auto-generated

    @NotNull(message = "Enrollment date is required")
    @Past(message = "Enrollment date must be in the past")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Clazz> classes = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();

    // Default constructor
    public Student() {
        super();
    }

    // Constructor with required fields (remove studentId param)
    public Student(String name, String email, LocalDate enrollmentDate) {
        super(name, email);
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

    public Set<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(Set<Clazz> classes) {
        this.classes = classes;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }

    // Helper methods for managing relationships
    public void addClazz(Clazz clazz) {
        classes.add(clazz);
        clazz.getStudents().add(this);
    }

    public void removeClazz(Clazz clazz) {
        classes.remove(clazz);
        clazz.getStudents().remove(this);
    }

    public void addRegistration(Registration registration) {
        registrations.add(registration);
        registration.setStudent(this);
    }

    public void removeRegistration(Registration registration) {
        registrations.remove(registration);
        registration.setStudent(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        // If both have IDs, use parent equals (which compares IDs)
        if (getId() != null && student.getId() != null) {
            return super.equals(o);
        }
        // If no IDs, compare by studentId
        return studentId != null && studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}