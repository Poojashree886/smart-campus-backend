package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.Complaint;
import com.smartcampus.backend.model.Staff;
import com.smartcampus.backend.repository.ComplaintRepository;
import com.smartcampus.backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private ComplaintRepository complaintRepository;

    // ========== API 1: Get Staff Profile ==========
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        
        response.put("success", true);
        response.put("data", staff);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 2: Get Assigned Complaints ==========
    @GetMapping("/complaints")
    public ResponseEntity<Map<String, Object>> getAssignedComplaints(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        
        List<Complaint> allComplaints = complaintRepository.findAll();
        List<Complaint> assignedComplaints = allComplaints.stream()
            .filter(c -> c.getAssignedTo() != null && c.getAssignedTo().equals(staff.getId()))
            .filter(c -> !"Resolved".equals(c.getStatus()))
            .collect(Collectors.toList());
        
        response.put("success", true);
        response.put("complaints", assignedComplaints);
        response.put("count", assignedComplaints.size());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 3: Get Resolved Complaints History ==========
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getResolvedHistory(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        
        List<Complaint> allComplaints = complaintRepository.findAll();
        List<Complaint> resolvedComplaints = allComplaints.stream()
            .filter(c -> c.getAssignedTo() != null && c.getAssignedTo().equals(staff.getId()))
            .filter(c -> "Resolved".equals(c.getStatus()))
            .collect(Collectors.toList());
        
        response.put("success", true);
        response.put("history", resolvedComplaints);
        response.put("count", resolvedComplaints.size());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 4: Get Staff Statistics ==========
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(@RequestParam Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        
        List<Complaint> allComplaints = complaintRepository.findAll();
        List<Complaint> myComplaints = allComplaints.stream()
            .filter(c -> c.getAssignedTo() != null && c.getAssignedTo().equals(staff.getId()))
            .collect(Collectors.toList());
        
        long totalAssigned = myComplaints.size();
        long pending = myComplaints.stream().filter(c -> "Pending".equals(c.getStatus())).count();
        long inProgress = myComplaints.stream().filter(c -> "In Progress".equals(c.getStatus())).count();
        long resolved = myComplaints.stream().filter(c -> "Resolved".equals(c.getStatus())).count();
        
        response.put("success", true);
        response.put("total_assigned", totalAssigned);
        response.put("pending", pending);
        response.put("in_progress", inProgress);
        response.put("resolved", resolved);
        response.put("current_status", staff.getStatus());
        
        return ResponseEntity.ok(response);
    }

    // ========== API 5: Update Complaint Status ==========
    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<Map<String, Object>> updateComplaintStatus(
            @RequestParam Long userId,
            @PathVariable Long id,
            @RequestParam String status) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        
        if (complaintOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Complaint not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Complaint complaint = complaintOpt.get();
        
        if (complaint.getAssignedTo() == null || !complaint.getAssignedTo().equals(staffOpt.get().getId())) {
            response.put("success", false);
            response.put("message", "This complaint is not assigned to you");
            return ResponseEntity.status(403).body(response);
        }
        
        complaint.setStatus(status);
        
        if ("Resolved".equals(status)) {
            complaint.setResolvedDate(LocalDate.now());
        }
        
        complaintRepository.save(complaint);
        
        response.put("success", true);
        response.put("message", "Complaint status updated to " + status);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 6: Update Own Availability Status ==========
    @PutMapping("/update-status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @RequestParam Long userId,
            @RequestParam String status) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        staff.setStatus(status);
        staffRepository.save(staff);
        
        response.put("success", true);
        response.put("message", "Status updated to " + status);
        response.put("new_status", status);
        
        return ResponseEntity.ok(response);
    }

    // ========== API 7: Upload Completion Proof ==========
    @PostMapping("/complaints/{id}/proof")
    public ResponseEntity<Map<String, Object>> uploadCompletionProof(
            @RequestParam Long userId,
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<Staff> staffOpt = staffRepository.findByUserId(userId);
        
        if (staffOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Staff not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Staff staff = staffOpt.get();
        
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        
        if (complaintOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Complaint not found");
            return ResponseEntity.status(404).body(response);
        }
        
        Complaint complaint = complaintOpt.get();
        
        if (complaint.getAssignedTo() == null || !complaint.getAssignedTo().equals(staff.getId())) {
            response.put("success", false);
            response.put("message", "This complaint is not assigned to you");
            return ResponseEntity.status(403).body(response);
        }
        
        String proofNotes = requestBody.get("proof_notes");
        String resolutionDetails = requestBody.get("resolution_details");
        
        String existingDesc = complaint.getDescription();
        String updatedDesc = existingDesc + "\n\n✅ RESOLUTION DETAILS:\n";
        updatedDesc += "Completed by: " + staff.getName() + "\n";
        updatedDesc += "Completion Date: " + LocalDate.now() + "\n";
        updatedDesc += "Resolution Notes: " + proofNotes + "\n";
        updatedDesc += "Details: " + resolutionDetails;
        
        complaint.setDescription(updatedDesc);
        complaint.setStatus("Resolved");
        complaint.setResolvedDate(LocalDate.now());
        
        staff.setAssignedTasks(staff.getAssignedTasks() + 1);
        staffRepository.save(staff);
        
        complaintRepository.save(complaint);
        
        response.put("success", true);
        response.put("message", "Completion proof uploaded successfully");
        response.put("complaint_id", complaint.getComplaintId());
        response.put("status", "Resolved");
        
        return ResponseEntity.ok(response);
    }
}