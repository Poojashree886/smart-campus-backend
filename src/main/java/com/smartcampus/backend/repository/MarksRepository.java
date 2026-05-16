package com.smartcampus.backend.repository;

import com.smartcampus.backend.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudentId(Long studentId);
    Optional<Marks> findByStudentIdAndSubject(Long studentId, String subject);
}