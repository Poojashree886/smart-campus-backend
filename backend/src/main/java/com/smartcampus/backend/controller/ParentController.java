package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.Attendance;
import com.smartcampus.backend.model.CalendarEvent;
import com.smartcampus.backend.model.Circular;
import com.smartcampus.backend.model.Complaint;
import com.smartcampus.backend.model.Feedback;
import com.smartcampus.backend.model.LeaveRequest;
import com.smartcampus.backend.model.Marks;
import com.smartcampus.backend.model.Parent;
import com.smartcampus.backend.model.Student;
import com.smartcampus.backend.repository.AttendanceRepository;
import com.smartcampus.backend.repository.CalendarEventRepository;
import com.smartcampus.backend.repository.CircularRepository;
import com.smartcampus.backend.repository.ComplaintRepository;
import com.smartcampus.backend.repository.FeedbackRepository;
import com.smartcampus.backend.repository.LeaveRepository;
import com.smartcampus.backend.repository.MarksRepository;
import com.smartcampus.backend.repository.ParentRepository;
import com.smartcampus.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/parent")
@CrossOrigin(origins = "*")
public class ParentController {

    @Autowired
    private ParentRepository parentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private MarksRepository marksRepository;
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private LeaveRepository leaveRepository;
    
    @Autowired
    private CalendarEventRepository calendarEventRepository;
    
    @Autowired
    private CircularRepository circularRepository;

    // ========== API 1: Get Parent Profile ==========
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Parent parent = parentOpt.get();
        
        response.put("success", true);
        response.put("data", parent);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 2: Get Parent's Children (Students) ==========
    @GetMapping("/children")
    public ResponseEntity<Map<String, Object>> getChildren(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Parent parent = parentOpt.get();
        
        List<Student> children = studentRepository.findByParentId(parent.getId());
        
        response.put("success", true);
        response.put("children", children);
        response.put("count", children.size());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 3: Get Child's Attendance ==========
    @GetMapping("/child/{studentId}/attendance")
    public ResponseEntity<Map<String, Object>> getChildAttendance(
            @RequestParam Long userId,
            @PathVariable Long studentId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Attendance> attendanceList = attendanceRepository.findByStudentId(studentId);
        
        int total = attendanceList.size();
        int present = (int) attendanceList.stream().filter(a -> "Present".equals(a.getStatus())).count();
        int percentage = total > 0 ? (present * 100 / total) : 0;
        
        response.put("success", true);
        response.put("student_name", student.getName());
        response.put("attendance", attendanceList);
        response.put("total_classes", total);
        response.put("present_count", present);
        response.put("percentage", percentage);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 4: Get Child's Marks ==========
    @GetMapping("/child/{studentId}/marks")
    public ResponseEntity<Map<String, Object>> getChildMarks(
            @RequestParam Long userId,
            @PathVariable Long studentId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Marks> marksList = marksRepository.findByStudentId(studentId);
        
        response.put("success", true);
        response.put("student_name", student.getName());
        response.put("marks", marksList);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 5: Get Child's Complaints ==========
    @GetMapping("/child/{studentId}/complaints")
    public ResponseEntity<Map<String, Object>> getChildComplaints(
            @RequestParam Long userId,
            @PathVariable Long studentId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Complaint> complaints = complaintRepository.findByStudentId(studentId);
        
        response.put("success", true);
        response.put("student_name", student.getName());
        response.put("complaints", complaints);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 6: Get Child's Advisor Feedback ==========
    @GetMapping("/child/{studentId}/feedback")
    public ResponseEntity<Map<String, Object>> getChildFeedback(
            @RequestParam Long userId,
            @PathVariable Long studentId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Feedback> feedbackList = feedbackRepository.findByStudentId(studentId);
        
        response.put("success", true);
        response.put("student_name", student.getName());
        response.put("feedback", feedbackList);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 7: Get Child's Leave History ==========
    @GetMapping("/child/{studentId}/leaves")
    public ResponseEntity<Map<String, Object>> getChildLeaves(
            @RequestParam Long userId,
            @PathVariable Long studentId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<LeaveRequest> leaves = leaveRepository.findByStudentId(studentId);
        
        response.put("success", true);
        response.put("student_name", student.getName());
        response.put("leaves", leaves);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 8: Get Academic Calendar ==========
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> getCalendar(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        List<CalendarEvent> events = calendarEventRepository.findAll();
        
        response.put("success", true);
        response.put("events", events);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 9: Get Circulars ==========
    @GetMapping("/circulars")
    public ResponseEntity<Map<String, Object>> getCirculars(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Parent> parentOpt = parentRepository.findByUserId(userId);
        
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        List<Circular> circulars = circularRepository.findByOrderByPostedDateDesc();
        
        response.put("success", true);
        response.put("circulars", circulars);
        
        return ResponseEntity.ok(response);
    }
}