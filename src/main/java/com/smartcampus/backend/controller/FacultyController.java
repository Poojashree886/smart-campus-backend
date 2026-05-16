package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.Attendance;
import com.smartcampus.backend.model.Circular;
import com.smartcampus.backend.model.Faculty;
import com.smartcampus.backend.model.Feedback;
import com.smartcampus.backend.model.LeaveRequest;
import com.smartcampus.backend.model.Marks;
import com.smartcampus.backend.model.Student;
import com.smartcampus.backend.repository.AttendanceRepository;
import com.smartcampus.backend.repository.CircularRepository;
import com.smartcampus.backend.repository.FacultyRepository;
import com.smartcampus.backend.repository.FeedbackRepository;
import com.smartcampus.backend.repository.LeaveRepository;
import com.smartcampus.backend.repository.MarksRepository;
import com.smartcampus.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private MarksRepository marksRepository;
    
    @Autowired
    private LeaveRepository leaveRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private CircularRepository circularRepository;

    // ========== API 1: Get Faculty Profile ==========
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        
        response.put("success", true);
        response.put("data", faculty);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 2: Get Subjects Taught by Faculty ==========
    @GetMapping("/subjects")
    public ResponseEntity<Map<String, Object>> getSubjects(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        
        List<Map<String, String>> subjects = new ArrayList<>();
        
        Map<String, String> subject1 = new HashMap<>();
        subject1.put("code", "CS301");
        subject1.put("name", "Java Programming");
        subject1.put("semester", "3");
        subjects.add(subject1);
        
        Map<String, String> subject2 = new HashMap<>();
        subject2.put("code", "CS302");
        subject2.put("name", "Database Management Systems");
        subject2.put("semester", "3");
        subjects.add(subject2);
        
        Map<String, String> subject3 = new HashMap<>();
        subject3.put("code", "CS303");
        subject3.put("name", "Cloud Computing");
        subject3.put("semester", "4");
        subjects.add(subject3);
        
        response.put("success", true);
        response.put("subjects", subjects);
        response.put("department", faculty.getDepartment());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 3: Get Students List ==========
    @GetMapping("/students")
    public ResponseEntity<Map<String, Object>> getStudents(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String section) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        List<Student> allStudents = studentRepository.findAll();
        
        List<Student> filteredStudents = allStudents.stream()
            .filter(s -> s.getDepartment().equals(faculty.getDepartment()))
            .collect(Collectors.toList());
        
        if (year != null) {
            filteredStudents = filteredStudents.stream()
                .filter(s -> s.getYear().equals(year))
                .collect(Collectors.toList());
        }
        
        if (section != null) {
            filteredStudents = filteredStudents.stream()
                .filter(s -> s.getSection().equals(section))
                .collect(Collectors.toList());
        }
        
        response.put("success", true);
        response.put("students", filteredStudents);
        response.put("count", filteredStudents.size());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 4: Mark Attendance (Fixed) ==========
@PostMapping("/attendance")
public ResponseEntity<Map<String, Object>> markAttendance(
        @RequestParam Long userId,
        @RequestBody Map<String, Object> attendanceRequest) {
    
    Map<String, Object> response = new HashMap<>();
    
    try {
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        
        String subject = (String) attendanceRequest.get("subject");
        String date = (String) attendanceRequest.get("date");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> attendanceList = (List<Map<String, Object>>) attendanceRequest.get("attendance");
        
        if (attendanceList == null || attendanceList.isEmpty()) {
            response.put("success", false);
            response.put("message", "Attendance list is empty");
            return ResponseEntity.status(400).body(response);
        }
        
        int marked = 0;
        
        for (Map<String, Object> record : attendanceList) {
            // Handle studentId as both Integer or String
            Long studentId = null;
            if (record.get("studentId") instanceof Integer) {
                studentId = ((Integer) record.get("studentId")).longValue();
            } else if (record.get("studentId") instanceof String) {
                studentId = Long.parseLong((String) record.get("studentId"));
            } else if (record.get("studentId") instanceof Long) {
                studentId = (Long) record.get("studentId");
            }
            
            String status = (String) record.get("status");
            
            if (studentId == null) {
                continue;
            }
            
            // Check if student exists
            Optional<Student> studentOpt = studentRepository.findById(studentId);
            if (studentOpt.isEmpty()) {
                continue;
            }
            
            Attendance attendance = new Attendance();
            attendance.setStudentId(studentId);
            attendance.setSubject(subject);
            attendance.setDate(LocalDate.parse(date));
            attendance.setStatus(status);
            attendance.setMarkedBy(faculty.getId());
            
            attendanceRepository.save(attendance);
            marked++;
        }
        
        response.put("success", true);
        response.put("message", "Attendance marked successfully");
        response.put("marked_count", marked);
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        e.printStackTrace();
        response.put("success", false);
        response.put("message", "Error: " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}

    // ========== API 5: Get Attendance Records ==========
    @GetMapping("/attendance/{subject}")
    public ResponseEntity<Map<String, Object>> getAttendanceRecords(
            @RequestParam Long userId,
            @PathVariable String subject) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        List<Attendance> attendanceRecords = attendanceRepository.findBySubject(subject);
        
        response.put("success", true);
        response.put("attendance", attendanceRecords);
        response.put("count", attendanceRecords.size());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 6: Upload Assignment Marks ==========
    // ========== API: Upload Assignment Marks ==========
@PostMapping("/marks/assignment")
public ResponseEntity<Map<String, Object>> uploadAssignmentMarks(
        @RequestParam Long userId,
        @RequestBody Map<String, Object> marksRequest) {
    
    Map<String, Object> response = new HashMap<>();
    
    try {
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        String subject = (String) marksRequest.get("subject");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> marksList = (List<Map<String, Object>>) marksRequest.get("marks");
        
        int updated = 0;
        
        for (Map<String, Object> record : marksList) {
            Long studentId = null;
            if (record.get("studentId") instanceof Integer) {
                studentId = ((Integer) record.get("studentId")).longValue();
            } else if (record.get("studentId") instanceof String) {
                studentId = Long.parseLong((String) record.get("studentId"));
            } else if (record.get("studentId") instanceof Long) {
                studentId = (Long) record.get("studentId");
            }
            
            Integer assignmentMarks = null;
            if (record.get("assignment_marks") instanceof Integer) {
                assignmentMarks = (Integer) record.get("assignment_marks");
            } else if (record.get("assignment_marks") instanceof String) {
                assignmentMarks = Integer.parseInt((String) record.get("assignment_marks"));
            }
            
            if (studentId == null || assignmentMarks == null) {
                continue;
            }
            
            // Find existing marks or create new
            Optional<Marks> existingMarksOpt = marksRepository.findByStudentIdAndSubject(studentId, subject);
            Marks marks;
            
            if (existingMarksOpt.isPresent()) {
                marks = existingMarksOpt.get();
            } else {
                marks = new Marks();
                marks.setStudentId(studentId);
                marks.setSubject(subject);
            }
            
            marks.setAssignmentMarks(assignmentMarks);
            
            // Calculate total
            int total = (marks.getAssignmentMarks() != null ? marks.getAssignmentMarks() : 0) +
                       (marks.getCiaMarks() != null ? marks.getCiaMarks() : 0) +
                       (marks.getSemesterMarks() != null ? marks.getSemesterMarks() : 0);
            marks.setTotalMarks(total);
            
            marksRepository.save(marks);
            updated++;
        }
        
        response.put("success", true);
        response.put("message", "Assignment marks uploaded successfully");
        response.put("updated_count", updated);
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        e.printStackTrace();
        response.put("success", false);
        response.put("message", "Error: " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}

    // ========== API 7: Upload CIA Marks ==========
    @PostMapping("/marks/cia")
    public ResponseEntity<Map<String, Object>> uploadCiaMarks(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> marksRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        String subject = (String) marksRequest.get("subject");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> marksList = (List<Map<String, Object>>) marksRequest.get("marks");
        
        int updated = 0;
        
        for (Map<String, Object> record : marksList) {
            Long studentId = Long.parseLong(record.get("studentId").toString());
            Integer ciaMarks = (Integer) record.get("cia_marks");
            
            List<Marks> existingMarks = marksRepository.findByStudentId(studentId);
            Marks marks = existingMarks.stream()
                .filter(m -> m.getSubject().equals(subject))
                .findFirst()
                .orElse(new Marks());
            
            marks.setStudentId(studentId);
            marks.setSubject(subject);
            marks.setCiaMarks(ciaMarks);
            
            int total = (marks.getAssignmentMarks() != null ? marks.getAssignmentMarks() : 0) +
                       (marks.getCiaMarks() != null ? marks.getCiaMarks() : 0) +
                       (marks.getSemesterMarks() != null ? marks.getSemesterMarks() : 0);
            marks.setTotalMarks(total);
            
            marksRepository.save(marks);
            updated++;
        }
        
        response.put("success", true);
        response.put("message", "CIA marks uploaded successfully");
        response.put("updated_count", updated);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 8: Upload Semester Marks ==========
    @PostMapping("/marks/semester")
    public ResponseEntity<Map<String, Object>> uploadSemesterMarks(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> marksRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        String subject = (String) marksRequest.get("subject");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> marksList = (List<Map<String, Object>>) marksRequest.get("marks");
        
        int updated = 0;
        
        for (Map<String, Object> record : marksList) {
            Long studentId = Long.parseLong(record.get("studentId").toString());
            Integer semesterMarks = (Integer) record.get("semester_marks");
            String grade = (String) record.get("grade");
            
            List<Marks> existingMarks = marksRepository.findByStudentId(studentId);
            Marks marks = existingMarks.stream()
                .filter(m -> m.getSubject().equals(subject))
                .findFirst()
                .orElse(new Marks());
            
            marks.setStudentId(studentId);
            marks.setSubject(subject);
            marks.setSemesterMarks(semesterMarks);
            marks.setGrade(grade);
            
            int total = (marks.getAssignmentMarks() != null ? marks.getAssignmentMarks() : 0) +
                       (marks.getCiaMarks() != null ? marks.getCiaMarks() : 0) +
                       (marks.getSemesterMarks() != null ? marks.getSemesterMarks() : 0);
            marks.setTotalMarks(total);
            
            marksRepository.save(marks);
            updated++;
        }
        
        response.put("success", true);
        response.put("message", "Semester marks uploaded successfully");
        response.put("updated_count", updated);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 9: Get Leave Requests ==========
    @GetMapping("/leaves")
    public ResponseEntity<Map<String, Object>> getLeaveRequests(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        List<LeaveRequest> allLeaves = leaveRepository.findAll();
        List<LeaveRequest> pendingLeaves = allLeaves.stream()
            .filter(l -> "Pending".equals(l.getStatus()))
            .collect(Collectors.toList());
        
        response.put("success", true);
        response.put("leaves", pendingLeaves);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 10: Approve/Reject Leave ==========
    @PutMapping("/leaves/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveLeave(
            @RequestParam Long userId,
            @PathVariable Long id,
            @RequestParam String action) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<LeaveRequest> leaveOpt = leaveRepository.findById(id);
        
        if (leaveOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Leave request not found");
            return ResponseEntity.status(404).body(response);
        }
        
        LeaveRequest leave = leaveOpt.get();
        
        if (action.equals("approve")) {
            leave.setStatus("Approved");
        } else if (action.equals("reject")) {
            leave.setStatus("Rejected");
        }
        
        leave.setApprovedBy(facultyOpt.get().getId());
        leaveRepository.save(leave);
        
        response.put("success", true);
        response.put("message", "Leave " + action + "d successfully");
        
        return ResponseEntity.ok(response);
    }

    // ========== API 11: Get Student Report (Download) ==========
    @GetMapping("/student-report/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentReport(
            @RequestParam Long userId,
            @PathVariable Long studentId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        List<Attendance> attendance = attendanceRepository.findByStudentId(studentId);
        List<Marks> marks = marksRepository.findByStudentId(studentId);
        
        int totalClasses = attendance.size();
        int present = (int) attendance.stream().filter(a -> "Present".equals(a.getStatus())).count();
        int attendancePercentage = totalClasses > 0 ? (present * 100 / totalClasses) : 0;
        
        response.put("success", true);
        response.put("student", student);
        response.put("attendance_percentage", attendancePercentage);
        response.put("marks", marks);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 12: Give Advisor Feedback to Student ==========
    @PostMapping("/feedback")
    public ResponseEntity<Map<String, Object>> giveFeedback(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> feedbackRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        
        Long studentId = Long.parseLong(feedbackRequest.get("studentId").toString());
        String positiveOpinion = (String) feedbackRequest.get("positive_opinion");
        String areasForImprovement = (String) feedbackRequest.get("areas_for_improvement");
        String behaviorRating = (String) feedbackRequest.get("behavior_rating");
        String classParticipation = (String) feedbackRequest.get("class_participation");
        String advisorSuggestions = (String) feedbackRequest.get("advisor_suggestions");
        
        Feedback feedback = new Feedback();
        feedback.setStudentId(studentId);
        feedback.setFacultyId(faculty.getId());
        feedback.setFeedbackDate(LocalDate.now());
        feedback.setPositiveOpinion(positiveOpinion);
        feedback.setAreasForImprovement(areasForImprovement);
        feedback.setBehaviorRating(behaviorRating);
        feedback.setClassParticipation(classParticipation);
        feedback.setAdvisorSuggestions(advisorSuggestions);
        
        feedbackRepository.save(feedback);
        
        response.put("success", true);
        response.put("message", "Feedback given successfully");
        
        return ResponseEntity.ok(response);
    }

    // ========== API 13: Post Circular ==========
    @PostMapping("/circulars")
    public ResponseEntity<Map<String, Object>> postCircular(
            @RequestParam Long userId,
            @RequestBody Map<String, String> circularRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        
        Circular circular = new Circular();
        circular.setTitle(circularRequest.get("title"));
        circular.setContent(circularRequest.get("content"));
        circular.setCircularType(circularRequest.get("circular_type"));
        circular.setPostedBy(faculty.getName());
        circular.setPostedDate(LocalDate.now());
        
        if (circularRequest.containsKey("is_urgent")) {
            circular.setIsUrgent(Boolean.parseBoolean(circularRequest.get("is_urgent")));
        }
        
        circularRepository.save(circular);
        
        response.put("success", true);
        response.put("message", "Circular posted successfully");
        
        return ResponseEntity.ok(response);
    }

    // ========== API 14: Get Circulars ==========
    @GetMapping("/circulars")
    public ResponseEntity<Map<String, Object>> getCirculars(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Faculty> facultyOpt = facultyRepository.findByUserId(userId);
        
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        List<Circular> circulars = circularRepository.findByOrderByPostedDateDesc();
        
        response.put("success", true);
        response.put("circulars", circulars);
        
        return ResponseEntity.ok(response);
    }
}