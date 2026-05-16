package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.Attendance;
import com.smartcampus.backend.model.CalendarEvent;
import com.smartcampus.backend.model.Circular;
import com.smartcampus.backend.model.Complaint;
import com.smartcampus.backend.model.Feedback;
import com.smartcampus.backend.model.LeaveRequest;
import com.smartcampus.backend.model.Marks;
import com.smartcampus.backend.model.Student;
import com.smartcampus.backend.repository.AttendanceRepository;
import com.smartcampus.backend.repository.CalendarEventRepository;
import com.smartcampus.backend.repository.CircularRepository;
import com.smartcampus.backend.repository.ComplaintRepository;
import com.smartcampus.backend.repository.FeedbackRepository;
import com.smartcampus.backend.repository.LeaveRepository;
import com.smartcampus.backend.repository.MarksRepository;
import com.smartcampus.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private MarksRepository marksRepository;
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private LeaveRepository leaveRepository;
    
    @Autowired
    private CalendarEventRepository calendarEventRepository;
    
    @Autowired
    private CircularRepository circularRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;

    // ========== API 1: Get Student Profile ==========
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        
        response.put("success", true);
        response.put("data", student);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 2: Get Student Attendance ==========
    @GetMapping("/attendance")
    public ResponseEntity<Map<String, Object>> getAttendance(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        
        List<Attendance> attendanceList = attendanceRepository.findByStudentId(student.getId());
        
        int total = attendanceList.size();
        int present = (int) attendanceList.stream().filter(a -> a.getStatus().equals("Present")).count();
        int percentage = total > 0 ? (present * 100 / total) : 0;
        
        response.put("success", true);
        response.put("attendance", attendanceList);
        response.put("total_classes", total);
        response.put("present_count", present);
        response.put("percentage", percentage);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 3: Get All Marks ==========
    @GetMapping("/marks/all")
    public ResponseEntity<Map<String, Object>> getAllMarks(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Marks> marksList = marksRepository.findByStudentId(student.getId());
        
        response.put("success", true);
        response.put("marks", marksList);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 4: Get Assignment Marks ==========
    @GetMapping("/marks/assignment")
    public ResponseEntity<Map<String, Object>> getAssignmentMarks(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Marks> marksList = marksRepository.findByStudentId(student.getId());
        
        List<Map<String, Object>> assignmentMarks = new java.util.ArrayList<>();
        for (Marks m : marksList) {
            Map<String, Object> subjectMarks = new HashMap<>();
            subjectMarks.put("subject", m.getSubject());
            subjectMarks.put("assignment_marks", m.getAssignmentMarks());
            subjectMarks.put("max_marks", 100);
            assignmentMarks.add(subjectMarks);
        }
        
        response.put("success", true);
        response.put("assignment_marks", assignmentMarks);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 5: Get CIA Marks ==========
    @GetMapping("/marks/cia")
    public ResponseEntity<Map<String, Object>> getCiaMarks(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Marks> marksList = marksRepository.findByStudentId(student.getId());
        
        List<Map<String, Object>> ciaMarks = new java.util.ArrayList<>();
        for (Marks m : marksList) {
            Map<String, Object> subjectMarks = new HashMap<>();
            subjectMarks.put("subject", m.getSubject());
            subjectMarks.put("cia_marks", m.getCiaMarks());
            subjectMarks.put("max_marks", 50);
            ciaMarks.add(subjectMarks);
        }
        
        response.put("success", true);
        response.put("cia_marks", ciaMarks);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 6: Get Semester Marks ==========
    @GetMapping("/marks/semester")
    public ResponseEntity<Map<String, Object>> getSemesterMarks(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Marks> marksList = marksRepository.findByStudentId(student.getId());
        
        List<Map<String, Object>> semesterMarks = new java.util.ArrayList<>();
        int totalScore = 0;
        int totalSubjects = marksList.size();
        
        for (Marks m : marksList) {
            Map<String, Object> subjectMarks = new HashMap<>();
            subjectMarks.put("subject", m.getSubject());
            subjectMarks.put("semester_marks", m.getSemesterMarks());
            subjectMarks.put("total_marks", m.getTotalMarks());
            subjectMarks.put("grade", m.getGrade());
            semesterMarks.add(subjectMarks);
            totalScore += m.getTotalMarks();
        }
        
        int average = totalSubjects > 0 ? totalScore / totalSubjects : 0;
        
        response.put("success", true);
        response.put("semester_marks", semesterMarks);
        response.put("average", average);
        response.put("cgpa", average / 10.0);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 7: Get Student Complaints ==========
    @GetMapping("/complaints")
    public ResponseEntity<Map<String, Object>> getComplaints(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Complaint> complaints = complaintRepository.findByStudentId(student.getId());
        
        response.put("success", true);
        response.put("complaints", complaints);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 8: Raise a New Complaint ==========
    @PostMapping("/complaints")
    public ResponseEntity<Map<String, Object>> raiseComplaint(
            @RequestParam Long userId,
            @RequestBody Map<String, String> complaintRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        
        Complaint complaint = new Complaint();
        
        long count = complaintRepository.count() + 1;
        complaint.setComplaintId("CMP" + String.format("%04d", count));
        
        complaint.setStudentId(student.getId());
        complaint.setCategory(complaintRequest.get("category"));
        complaint.setLocation(complaintRequest.get("location"));
        complaint.setDescription(complaintRequest.get("description"));
        complaint.setStatus("Pending");
        complaint.setCreatedDate(LocalDate.now());
        
        complaintRepository.save(complaint);
        
        response.put("success", true);
        response.put("message", "Complaint raised successfully");
        response.put("complaintId", complaint.getComplaintId());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 9: Get Student Leave Requests ==========
    @GetMapping("/leaves")
    public ResponseEntity<Map<String, Object>> getLeaveRequests(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<LeaveRequest> leaves = leaveRepository.findByStudentId(student.getId());
        
        response.put("success", true);
        response.put("leaves", leaves);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 10: Apply for Leave ==========

    // ========== API: Apply for Leave (Fixed) ==========
@PostMapping("/leaves")
public ResponseEntity<Map<String, Object>> applyLeave(
        @RequestParam Long userId,
        @RequestBody Map<String, String> leaveRequest) {
    
    Map<String, Object> response = new HashMap<>();
    
    try {
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found for userId: " + userId);
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        
        String fromDateStr = leaveRequest.get("fromDate");
        String toDateStr = leaveRequest.get("toDate");
        String reason = leaveRequest.get("reason");
        
        if (fromDateStr == null || toDateStr == null) {
            response.put("success", false);
            response.put("message", "From date and to date are required");
            return ResponseEntity.status(400).body(response);
        }
        
        LeaveRequest leave = new LeaveRequest();
        leave.setStudentId(student.getId());
        leave.setFromDate(LocalDate.parse(fromDateStr));
        leave.setToDate(LocalDate.parse(toDateStr));
        leave.setReason(reason != null ? reason : "");
        leave.setStatus("Pending");
        
        LeaveRequest savedLeave = leaveRepository.save(leave);
        
        response.put("success", true);
        response.put("message", "Leave request submitted successfully");
        response.put("leaveId", savedLeave.getId());
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        e.printStackTrace();
        response.put("success", false);
        response.put("message", "Error: " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}

    // ========== API 11: Get Calendar Events ==========
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> getCalendarEvents(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        List<CalendarEvent> events = calendarEventRepository.findAll();
        
        response.put("success", true);
        response.put("events", events);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 12: Get Circulars ==========
    @GetMapping("/circulars")
    public ResponseEntity<Map<String, Object>> getCirculars(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        List<Circular> circulars = circularRepository.findByOrderByPostedDateDesc();
        
        response.put("success", true);
        response.put("circulars", circulars);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 13: Get Advisor Feedback ==========
    @GetMapping("/feedback")
    public ResponseEntity<Map<String, Object>> getFeedback(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Feedback> feedbackList = feedbackRepository.findByStudentId(student.getId());
        
        response.put("success", true);
        response.put("feedback", feedbackList);
        
        return ResponseEntity.ok(response);
    }
}