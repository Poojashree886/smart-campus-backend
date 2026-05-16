package com.smartcampus.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "marks")
public class Marks {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id")
    private Long studentId;
    
    private String subject;
    
    @Column(name = "assignment_marks")
    private Integer assignmentMarks;
    
    @Column(name = "cia_marks")
    private Integer ciaMarks;
    
    @Column(name = "semester_marks")
    private Integer semesterMarks;
    
    @Column(name = "total_marks")
    private Integer totalMarks;
    
    private String grade;
    
    @Column(name = "academic_year")
    private String academicYear;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public Integer getAssignmentMarks() { return assignmentMarks; }
    public void setAssignmentMarks(Integer assignmentMarks) { this.assignmentMarks = assignmentMarks; }
    
    public Integer getCiaMarks() { return ciaMarks; }
    public void setCiaMarks(Integer ciaMarks) { this.ciaMarks = ciaMarks; }
    
    public Integer getSemesterMarks() { return semesterMarks; }
    public void setSemesterMarks(Integer semesterMarks) { this.semesterMarks = semesterMarks; }
    
    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}