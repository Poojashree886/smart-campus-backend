package com.smartcampus.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "advisor_feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id")
    private Long studentId;
    
    @Column(name = "faculty_id")
    private Long facultyId;
    
    @Column(name = "feedback_date")
    private LocalDate feedbackDate;
    
    @Column(name = "positive_opinion", columnDefinition = "TEXT")
    private String positiveOpinion;
    
    @Column(name = "areas_for_improvement", columnDefinition = "TEXT")
    private String areasForImprovement;
    
    @Column(name = "behavior_rating")
    private String behaviorRating;
    
    @Column(name = "class_participation")
    private String classParticipation;
    
    @Column(name = "advisor_suggestions", columnDefinition = "TEXT")
    private String advisorSuggestions;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
    
    public LocalDate getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDate feedbackDate) { this.feedbackDate = feedbackDate; }
    
    public String getPositiveOpinion() { return positiveOpinion; }
    public void setPositiveOpinion(String positiveOpinion) { this.positiveOpinion = positiveOpinion; }
    
    public String getAreasForImprovement() { return areasForImprovement; }
    public void setAreasForImprovement(String areasForImprovement) { this.areasForImprovement = areasForImprovement; }
    
    public String getBehaviorRating() { return behaviorRating; }
    public void setBehaviorRating(String behaviorRating) { this.behaviorRating = behaviorRating; }
    
    public String getClassParticipation() { return classParticipation; }
    public void setClassParticipation(String classParticipation) { this.classParticipation = classParticipation; }
    
    public String getAdvisorSuggestions() { return advisorSuggestions; }
    public void setAdvisorSuggestions(String advisorSuggestions) { this.advisorSuggestions = advisorSuggestions; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}