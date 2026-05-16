package com.smartcampus.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_settings")
public class SystemSettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // General Settings
    @Column(name = "college_name")
    private String collegeName;
    
    @Column(name = "college_logo")
    private String collegeLogo;
    
    @Column(name = "contact_email")
    private String contactEmail;
    
    @Column(name = "contact_phone")
    private String contactPhone;
    
    @Column(name = "college_address")
    private String collegeAddress;
    
    // Academic Settings
    @Column(name = "academic_year")
    private String academicYear;
    
    @Column(name = "current_semester")
    private String currentSemester;
    
    @Column(name = "min_attendance")
    private Integer minAttendance;
    
    @Column(name = "grading_system")
    private String gradingSystem;
    
    // User Management Settings
    @Column(name = "student_registration")
    private Boolean studentRegistration;
    
    @Column(name = "faculty_registration")
    private Boolean facultyRegistration;
    
    @Column(name = "default_password")
    private String defaultPassword;
    
    @Column(name = "session_timeout")
    private Integer sessionTimeout;
    
    // Notification Settings
    @Column(name = "email_notifications")
    private Boolean emailNotifications;
    
    @Column(name = "sms_notifications")
    private Boolean smsNotifications;
    
    @Column(name = "push_notifications")
    private Boolean pushNotifications;
    
    @Column(name = "smtp_server")
    private String smtpServer;
    
    @Column(name = "smtp_port")
    private Integer smtpPort;
    
    // Security Settings
    @Column(name = "two_factor_auth")
    private Boolean twoFactorAuth;
    
    @Column(name = "password_expiry")
    private Integer passwordExpiry;
    
    @Column(name = "max_login_attempts")
    private Integer maxLoginAttempts;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    
    public String getCollegeLogo() { return collegeLogo; }
    public void setCollegeLogo(String collegeLogo) { this.collegeLogo = collegeLogo; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public String getCollegeAddress() { return collegeAddress; }
    public void setCollegeAddress(String collegeAddress) { this.collegeAddress = collegeAddress; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(String currentSemester) { this.currentSemester = currentSemester; }
    
    public Integer getMinAttendance() { return minAttendance; }
    public void setMinAttendance(Integer minAttendance) { this.minAttendance = minAttendance; }
    
    public String getGradingSystem() { return gradingSystem; }
    public void setGradingSystem(String gradingSystem) { this.gradingSystem = gradingSystem; }
    
    public Boolean getStudentRegistration() { return studentRegistration; }
    public void setStudentRegistration(Boolean studentRegistration) { this.studentRegistration = studentRegistration; }
    
    public Boolean getFacultyRegistration() { return facultyRegistration; }
    public void setFacultyRegistration(Boolean facultyRegistration) { this.facultyRegistration = facultyRegistration; }
    
    public String getDefaultPassword() { return defaultPassword; }
    public void setDefaultPassword(String defaultPassword) { this.defaultPassword = defaultPassword; }
    
    public Integer getSessionTimeout() { return sessionTimeout; }
    public void setSessionTimeout(Integer sessionTimeout) { this.sessionTimeout = sessionTimeout; }
    
    public Boolean getEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(Boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    
    public Boolean getSmsNotifications() { return smsNotifications; }
    public void setSmsNotifications(Boolean smsNotifications) { this.smsNotifications = smsNotifications; }
    
    public Boolean getPushNotifications() { return pushNotifications; }
    public void setPushNotifications(Boolean pushNotifications) { this.pushNotifications = pushNotifications; }
    
    public String getSmtpServer() { return smtpServer; }
    public void setSmtpServer(String smtpServer) { this.smtpServer = smtpServer; }
    
    public Integer getSmtpPort() { return smtpPort; }
    public void setSmtpPort(Integer smtpPort) { this.smtpPort = smtpPort; }
    
    public Boolean getTwoFactorAuth() { return twoFactorAuth; }
    public void setTwoFactorAuth(Boolean twoFactorAuth) { this.twoFactorAuth = twoFactorAuth; }
    
    public Integer getPasswordExpiry() { return passwordExpiry; }
    public void setPasswordExpiry(Integer passwordExpiry) { this.passwordExpiry = passwordExpiry; }
    
    public Integer getMaxLoginAttempts() { return maxLoginAttempts; }
    public void setMaxLoginAttempts(Integer maxLoginAttempts) { this.maxLoginAttempts = maxLoginAttempts; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}