package com.smartcampus.backend.repository;

import com.smartcampus.backend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Get all attendance records for a student
    List<Attendance> findByStudentId(Long studentId);
    
    // Get attendance by subject
    List<Attendance> findByStudentIdAndSubject(Long studentId, String subject);

    List<Attendance> findBySubject(String subject);
    
}