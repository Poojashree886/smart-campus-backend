package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.*;
import com.smartcampus.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private FacultyRepository facultyRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SystemSettingsRepository settingsRepository;

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    // ==================== STATISTICS ====================
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("total_students", studentRepository.count());
        response.put("total_faculty", facultyRepository.count());
        response.put("total_staff", staffRepository.count());
        response.put("total_complaints", complaintRepository.count());
        
        List<Complaint> allComplaints = complaintRepository.findAll();
        long pendingComplaints = allComplaints.stream().filter(c -> "Pending".equals(c.getStatus())).count();
        response.put("pending_complaints", pendingComplaints);
        
        return ResponseEntity.ok(response);
    }

    // ==================== STUDENT CRUD ====================
    @GetMapping("/students")
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("students", studentRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        response.put("success", true);
        response.put("student", student.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/students")
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Map<String, Object> studentData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student student = new Student();
            student.setName((String) studentData.get("name"));
            student.setRegisterNumber((String) studentData.get("registerNumber"));
            student.setDepartment((String) studentData.get("department"));
            student.setYear((Integer) studentData.get("year"));
            student.setSection((String) studentData.get("section"));
            student.setGender((String) studentData.get("gender"));
            student.setPhone((String) studentData.get("phone"));
            student.setParentName((String) studentData.get("parentName"));
            student.setParentPhone((String) studentData.get("parentPhone"));
            student.setResidence((String) studentData.get("residence"));
            student.setUserId(null);
            
            response.put("success", true);
            response.put("student", studentRepository.save(student));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @RequestBody Map<String, Object> studentData) {
        Map<String, Object> response = new HashMap<>();
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Student student = studentOpt.get();
        if (studentData.containsKey("name")) student.setName((String) studentData.get("name"));
        if (studentData.containsKey("department")) student.setDepartment((String) studentData.get("department"));
        if (studentData.containsKey("year")) student.setYear((Integer) studentData.get("year"));
        if (studentData.containsKey("section")) student.setSection((String) studentData.get("section"));
        if (studentData.containsKey("phone")) student.setPhone((String) studentData.get("phone"));
        
        response.put("success", true);
        response.put("student", studentRepository.save(student));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (studentRepository.findById(id).isEmpty()) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(404).body(response);
        }
        studentRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "Student deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ==================== FACULTY CRUD ====================
    @GetMapping("/faculty")
    public ResponseEntity<Map<String, Object>> getAllFaculty() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("faculty", facultyRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/faculty/{id}")
    public ResponseEntity<Map<String, Object>> getFacultyById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        response.put("success", true);
        response.put("faculty", faculty.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/faculty")
    public ResponseEntity<Map<String, Object>> addFaculty(@RequestBody Map<String, Object> facultyData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Faculty faculty = new Faculty();
            faculty.setName((String) facultyData.get("name"));
            faculty.setEmployeeId((String) facultyData.get("employeeId"));
            faculty.setDepartment((String) facultyData.get("department"));
            faculty.setDesignation((String) facultyData.get("designation"));
            faculty.setPhone((String) facultyData.get("phone"));
            faculty.setEmail((String) facultyData.get("email"));
            faculty.setUserId(null);
            
            response.put("success", true);
            response.put("faculty", facultyRepository.save(faculty));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/faculty/{id}")
    public ResponseEntity<Map<String, Object>> updateFaculty(@PathVariable Long id, @RequestBody Map<String, Object> facultyData) {
        Map<String, Object> response = new HashMap<>();
        Optional<Faculty> facultyOpt = facultyRepository.findById(id);
        if (facultyOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Faculty faculty = facultyOpt.get();
        if (facultyData.containsKey("name")) faculty.setName((String) facultyData.get("name"));
        if (facultyData.containsKey("department")) faculty.setDepartment((String) facultyData.get("department"));
        if (facultyData.containsKey("designation")) faculty.setDesignation((String) facultyData.get("designation"));
        if (facultyData.containsKey("phone")) faculty.setPhone((String) facultyData.get("phone"));
        
        response.put("success", true);
        response.put("faculty", facultyRepository.save(faculty));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/faculty/{id}")
    public ResponseEntity<Map<String, Object>> deleteFaculty(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (facultyRepository.findById(id).isEmpty()) {
            response.put("success", false);
            response.put("message", "Faculty not found");
            return ResponseEntity.status(404).body(response);
        }
        facultyRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "Faculty deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ==================== STAFF CRUD ====================
    @GetMapping("/staff")
    public ResponseEntity<Map<String, Object>> getAllStaff() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("staff", staffRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<Map<String, Object>> getStaffById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        response.put("success", true);
        response.put("staff", staff.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/staff")
    public ResponseEntity<Map<String, Object>> addStaff(@RequestBody Map<String, Object> staffData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Staff staff = new Staff();
            staff.setName((String) staffData.get("name"));
            staff.setStaffId((String) staffData.get("staffId"));
            staff.setCategory((String) staffData.get("category"));
            staff.setPhone((String) staffData.get("phone"));
            staff.setStatus("Available");
            staff.setAssignedTasks(0);
            staff.setUserId(null);
            
            response.put("success", true);
            response.put("staff", staffRepository.save(staff));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<Map<String, Object>> updateStaff(@PathVariable Long id, @RequestBody Map<String, Object> staffData) {
        Map<String, Object> response = new HashMap<>();
        Optional<Staff> staffOpt = staffRepository.findById(id);
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        if (staffData.containsKey("name")) staff.setName((String) staffData.get("name"));
        if (staffData.containsKey("category")) staff.setCategory((String) staffData.get("category"));
        if (staffData.containsKey("phone")) staff.setPhone((String) staffData.get("phone"));
        if (staffData.containsKey("status")) staff.setStatus((String) staffData.get("status"));
        
        response.put("success", true);
        response.put("staff", staffRepository.save(staff));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Map<String, Object>> deleteStaff(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (staffRepository.findById(id).isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        staffRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "Staff deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ==================== COMPLAINTS ====================
    @GetMapping("/complaints")
    public ResponseEntity<Map<String, Object>> getAllComplaints() {
        Map<String, Object> response = new HashMap<>();
        List<Complaint> complaints = complaintRepository.findAll();
        response.put("success", true);
        response.put("complaints", complaints);
        response.put("count", complaints.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/complaints/{id}")
    public ResponseEntity<Map<String, Object>> getComplaintById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Complaint> complaint = complaintRepository.findById(id);
        if (complaint.isEmpty()) {
            response.put("success", false);
            response.put("message", "Complaint not found");
            return ResponseEntity.status(404).body(response);
        }
        response.put("success", true);
        response.put("complaint", complaint.get());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<Map<String, Object>> updateComplaintStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        Map<String, Object> response = new HashMap<>();
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        
        if (complaintOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Complaint not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Complaint complaint = complaintOpt.get();
        complaint.setStatus(status);
        
        if ("Resolved".equals(status)) {
            complaint.setResolvedDate(java.time.LocalDate.now());
        }
        
        response.put("success", true);
        response.put("message", "Complaint status updated to " + status);
        response.put("complaint", complaintRepository.save(complaint));
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/complaints/{id}/assign")
    public ResponseEntity<Map<String, Object>> assignComplaint(
            @PathVariable Long id,
            @RequestParam Long staffId) {
        
        Map<String, Object> response = new HashMap<>();
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        
        if (complaintOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Complaint not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Complaint complaint = complaintOpt.get();
        complaint.setAssignedTo(staffId);
        complaint.setStatus("In Progress");
        
        response.put("success", true);
        response.put("message", "Complaint assigned to " + staffOpt.get().getName());
        response.put("complaint", complaintRepository.save(complaint));
        
        return ResponseEntity.ok(response);
    }

    // ==================== COURSES CRUD ====================
    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("courses", courseRepository.findAll());
        response.put("count", courseRepository.count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            response.put("success", false);
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }
        response.put("success", true);
        response.put("course", course.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/courses")
    public ResponseEntity<Map<String, Object>> addCourse(@RequestBody Map<String, Object> courseData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Course course = new Course();
            course.setCourseCode((String) courseData.get("courseCode"));
            course.setCourseName((String) courseData.get("courseName"));
            course.setDepartment((String) courseData.get("department"));
            course.setCredits((Integer) courseData.get("credits"));
            course.setSemester((Integer) courseData.get("semester"));
            course.setType((String) courseData.get("type"));
            
            response.put("success", true);
            response.put("course", courseRepository.save(course));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable Long id, @RequestBody Map<String, Object> courseData) {
        Map<String, Object> response = new HashMap<>();
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Course course = courseOpt.get();
        if (courseData.containsKey("courseName")) course.setCourseName((String) courseData.get("courseName"));
        if (courseData.containsKey("courseCode")) course.setCourseCode((String) courseData.get("courseCode"));
        if (courseData.containsKey("department")) course.setDepartment((String) courseData.get("department"));
        if (courseData.containsKey("credits")) course.setCredits((Integer) courseData.get("credits"));
        if (courseData.containsKey("semester")) course.setSemester((Integer) courseData.get("semester"));
        if (courseData.containsKey("type")) course.setType((String) courseData.get("type"));
        
        response.put("success", true);
        response.put("course", courseRepository.save(course));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (courseRepository.findById(id).isEmpty()) {
            response.put("success", false);
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }
        courseRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "Course deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ==================== PARENTS ====================
    @GetMapping("/parents")
    public ResponseEntity<Map<String, Object>> getAllParents() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("parents", parentRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parents/{id}")
    public ResponseEntity<Map<String, Object>> getParentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Parent> parent = parentRepository.findById(id);
        if (parent.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        response.put("success", true);
        response.put("parent", parent.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parents")
    public ResponseEntity<Map<String, Object>> addParent(@RequestBody Map<String, Object> parentData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Parent parent = new Parent();
            parent.setName((String) parentData.get("name"));
            parent.setPhone((String) parentData.get("phone"));
            parent.setEmail((String) parentData.get("email"));
            parent.setUserId(null);
            
            response.put("success", true);
            response.put("parent", parentRepository.save(parent));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/parents/{id}")
    public ResponseEntity<Map<String, Object>> updateParent(@PathVariable Long id, @RequestBody Map<String, Object> parentData) {
        Map<String, Object> response = new HashMap<>();
        Optional<Parent> parentOpt = parentRepository.findById(id);
        if (parentOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Parent parent = parentOpt.get();
        if (parentData.containsKey("name")) parent.setName((String) parentData.get("name"));
        if (parentData.containsKey("phone")) parent.setPhone((String) parentData.get("phone"));
        if (parentData.containsKey("email")) parent.setEmail((String) parentData.get("email"));
        
        response.put("success", true);
        response.put("parent", parentRepository.save(parent));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/parents/{id}")
    public ResponseEntity<Map<String, Object>> deleteParent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (parentRepository.findById(id).isEmpty()) {
            response.put("success", false);
            response.put("message", "Parent not found");
            return ResponseEntity.status(404).body(response);
        }
        parentRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "Parent deleted successfully");
        return ResponseEntity.ok(response);
    }


    // ==================== DEPARTMENTS CRUD ====================

// Get all departments
@GetMapping("/departments")
public ResponseEntity<Map<String, Object>> getAllDepartments() {
    Map<String, Object> response = new HashMap<>();
    List<Department> departments = departmentRepository.findAll();
    response.put("success", true);
    response.put("departments", departments);
    response.put("count", departments.size());
    return ResponseEntity.ok(response);
}

// Get department by ID
@GetMapping("/departments/{id}")
public ResponseEntity<Map<String, Object>> getDepartmentById(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();
    Optional<Department> department = departmentRepository.findById(id);
    if (department.isEmpty()) {
        response.put("success", false);
        response.put("message", "Department not found");
        return ResponseEntity.status(404).body(response);
    }
    response.put("success", true);
    response.put("department", department.get());
    return ResponseEntity.ok(response);
}

// Add new department
@PostMapping("/departments")
public ResponseEntity<Map<String, Object>> addDepartment(@RequestBody Map<String, Object> deptData) {
    Map<String, Object> response = new HashMap<>();
    try {
        Department department = new Department();
        department.setDeptCode((String) deptData.get("deptCode"));
        department.setDeptName((String) deptData.get("deptName"));
        department.setHod((String) deptData.get("hod"));
        department.setFacultyCount((Integer) deptData.get("facultyCount"));
        department.setStudentCount((Integer) deptData.get("studentCount"));
        department.setDescription((String) deptData.get("description"));
        
        response.put("success", true);
        response.put("department", departmentRepository.save(department));
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        response.put("success", false);
        response.put("message", e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}

// Update department
@PutMapping("/departments/{id}")
public ResponseEntity<Map<String, Object>> updateDepartment(@PathVariable Long id, @RequestBody Map<String, Object> deptData) {
    Map<String, Object> response = new HashMap<>();
    Optional<Department> deptOpt = departmentRepository.findById(id);
    if (deptOpt.isEmpty()) {
        response.put("success", false);
        response.put("message", "Department not found");
        return ResponseEntity.status(404).body(response);
    }
    
    Department department = deptOpt.get();
    if (deptData.containsKey("deptName")) department.setDeptName((String) deptData.get("deptName"));
    if (deptData.containsKey("hod")) department.setHod((String) deptData.get("hod"));
    if (deptData.containsKey("facultyCount")) department.setFacultyCount((Integer) deptData.get("facultyCount"));
    if (deptData.containsKey("studentCount")) department.setStudentCount((Integer) deptData.get("studentCount"));
    if (deptData.containsKey("description")) department.setDescription((String) deptData.get("description"));
    
    response.put("success", true);
    response.put("department", departmentRepository.save(department));
    return ResponseEntity.ok(response);
}

// Delete department
@DeleteMapping("/departments/{id}")
public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();
    if (departmentRepository.findById(id).isEmpty()) {
        response.put("success", false);
        response.put("message", "Department not found");
        return ResponseEntity.status(404).body(response);
    }
    departmentRepository.deleteById(id);
    response.put("success", true);
    response.put("message", "Department deleted successfully");
    return ResponseEntity.ok(response);
}

// ==================== SYSTEM SETTINGS ====================

// Get system settings
@GetMapping("/settings")
public ResponseEntity<Map<String, Object>> getSystemSettings() {
    Map<String, Object> response = new HashMap<>();
    
    // Get settings (assuming only one record with id=1)
    Optional<SystemSettings> settingsOpt = settingsRepository.findById(1L);
    
    if (settingsOpt.isEmpty()) {
        response.put("success", false);
        response.put("message", "Settings not found");
        return ResponseEntity.status(404).body(response);
    }
    
    response.put("success", true);
    response.put("settings", settingsOpt.get());
    return ResponseEntity.ok(response);
}

// Update system settings
@PutMapping("/settings")
public ResponseEntity<Map<String, Object>> updateSystemSettings(@RequestBody Map<String, Object> settingsData) {
    Map<String, Object> response = new HashMap<>();
    
    Optional<SystemSettings> settingsOpt = settingsRepository.findById(1L);
    
    if (settingsOpt.isEmpty()) {
        response.put("success", false);
        response.put("message", "Settings not found");
        return ResponseEntity.status(404).body(response);
    }
    
    SystemSettings settings = settingsOpt.get();
    
    // General Settings
    if (settingsData.containsKey("collegeName")) settings.setCollegeName((String) settingsData.get("collegeName"));
    if (settingsData.containsKey("contactEmail")) settings.setContactEmail((String) settingsData.get("contactEmail"));
    if (settingsData.containsKey("contactPhone")) settings.setContactPhone((String) settingsData.get("contactPhone"));
    if (settingsData.containsKey("collegeAddress")) settings.setCollegeAddress((String) settingsData.get("collegeAddress"));
    
    // Academic Settings
    if (settingsData.containsKey("academicYear")) settings.setAcademicYear((String) settingsData.get("academicYear"));
    if (settingsData.containsKey("currentSemester")) settings.setCurrentSemester((String) settingsData.get("currentSemester"));
    if (settingsData.containsKey("minAttendance")) settings.setMinAttendance((Integer) settingsData.get("minAttendance"));
    if (settingsData.containsKey("gradingSystem")) settings.setGradingSystem((String) settingsData.get("gradingSystem"));
    
    // User Management Settings
    if (settingsData.containsKey("studentRegistration")) settings.setStudentRegistration((Boolean) settingsData.get("studentRegistration"));
    if (settingsData.containsKey("facultyRegistration")) settings.setFacultyRegistration((Boolean) settingsData.get("facultyRegistration"));
    if (settingsData.containsKey("defaultPassword")) settings.setDefaultPassword((String) settingsData.get("defaultPassword"));
    if (settingsData.containsKey("sessionTimeout")) settings.setSessionTimeout((Integer) settingsData.get("sessionTimeout"));
    
    // Notification Settings
    if (settingsData.containsKey("emailNotifications")) settings.setEmailNotifications((Boolean) settingsData.get("emailNotifications"));
    if (settingsData.containsKey("smsNotifications")) settings.setSmsNotifications((Boolean) settingsData.get("smsNotifications"));
    if (settingsData.containsKey("pushNotifications")) settings.setPushNotifications((Boolean) settingsData.get("pushNotifications"));
    if (settingsData.containsKey("smtpServer")) settings.setSmtpServer((String) settingsData.get("smtpServer"));
    if (settingsData.containsKey("smtpPort")) settings.setSmtpPort((Integer) settingsData.get("smtpPort"));
    
    // Security Settings
    if (settingsData.containsKey("twoFactorAuth")) settings.setTwoFactorAuth((Boolean) settingsData.get("twoFactorAuth"));
    if (settingsData.containsKey("passwordExpiry")) settings.setPasswordExpiry((Integer) settingsData.get("passwordExpiry"));
    if (settingsData.containsKey("maxLoginAttempts")) settings.setMaxLoginAttempts((Integer) settingsData.get("maxLoginAttempts"));
    
    settings.setUpdatedAt(java.time.LocalDateTime.now());
    
    response.put("success", true);
    response.put("message", "Settings updated successfully");
    response.put("settings", settingsRepository.save(settings));
    
    return ResponseEntity.ok(response);
}
    
// ==================== CALENDAR EVENTS CRUD ====================

// Get all calendar events
@GetMapping("/calendar")
public ResponseEntity<Map<String, Object>> getAllCalendarEvents() {
    Map<String, Object> response = new HashMap<>();
    List<CalendarEvent> events = calendarEventRepository.findByOrderByStartDateAsc();
    response.put("success", true);
    response.put("events", events);
    response.put("count", events.size());
    return ResponseEntity.ok(response);
}

// Get calendar event by ID
@GetMapping("/calendar/{id}")
public ResponseEntity<Map<String, Object>> getCalendarEventById(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();
    Optional<CalendarEvent> event = calendarEventRepository.findById(id);
    if (event.isEmpty()) {
        response.put("success", false);
        response.put("message", "Event not found");
        return ResponseEntity.status(404).body(response);
    }
    response.put("success", true);
    response.put("event", event.get());
    return ResponseEntity.ok(response);
}

// Add new calendar event
@PostMapping("/calendar")
public ResponseEntity<Map<String, Object>> addCalendarEvent(@RequestBody Map<String, Object> eventData) {
    Map<String, Object> response = new HashMap<>();
    try {
        CalendarEvent event = new CalendarEvent();
        event.setTitle((String) eventData.get("title"));
        event.setType((String) eventData.get("type"));
        event.setStartDate(java.time.LocalDate.parse((String) eventData.get("startDate")));
        
        if (eventData.containsKey("endDate") && eventData.get("endDate") != null) {
            event.setEndDate(java.time.LocalDate.parse((String) eventData.get("endDate")));
        }
        
        event.setDescription((String) eventData.get("description"));
        
        response.put("success", true);
        response.put("event", calendarEventRepository.save(event));
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        response.put("success", false);
        response.put("message", e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}

// Update calendar event
@PutMapping("/calendar/{id}")
public ResponseEntity<Map<String, Object>> updateCalendarEvent(@PathVariable Long id, @RequestBody Map<String, Object> eventData) {
    Map<String, Object> response = new HashMap<>();
    Optional<CalendarEvent> eventOpt = calendarEventRepository.findById(id);
    if (eventOpt.isEmpty()) {
        response.put("success", false);
        response.put("message", "Event not found");
        return ResponseEntity.status(404).body(response);
    }
    
    CalendarEvent event = eventOpt.get();
    if (eventData.containsKey("title")) event.setTitle((String) eventData.get("title"));
    if (eventData.containsKey("type")) event.setType((String) eventData.get("type"));
    if (eventData.containsKey("startDate")) event.setStartDate(java.time.LocalDate.parse((String) eventData.get("startDate")));
    if (eventData.containsKey("endDate")) event.setEndDate(java.time.LocalDate.parse((String) eventData.get("endDate")));
    if (eventData.containsKey("description")) event.setDescription((String) eventData.get("description"));
    
    response.put("success", true);
    response.put("event", calendarEventRepository.save(event));
    return ResponseEntity.ok(response);
}

// Delete calendar event
@DeleteMapping("/calendar/{id}")
public ResponseEntity<Map<String, Object>> deleteCalendarEvent(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();
    if (calendarEventRepository.findById(id).isEmpty()) {
        response.put("success", false);
        response.put("message", "Event not found");
        return ResponseEntity.status(404).body(response);
    }
    calendarEventRepository.deleteById(id);
    response.put("success", true);
    response.put("message", "Event deleted successfully");
    return ResponseEntity.ok(response);
}

}