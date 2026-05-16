package com.smartcampus.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dept_code", unique = true)
    private String deptCode;
    
    @Column(name = "dept_name")
    private String deptName;
    
    private String hod;
    
    @Column(name = "faculty_count")
    private Integer facultyCount = 0;
    
    @Column(name = "student_count")
    private Integer studentCount = 0;
    
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    
    public String getHod() { return hod; }
    public void setHod(String hod) { this.hod = hod; }
    
    public Integer getFacultyCount() { return facultyCount; }
    public void setFacultyCount(Integer facultyCount) { this.facultyCount = facultyCount; }
    
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}