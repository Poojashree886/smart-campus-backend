package com.smartcampus.backend.repository;

import com.smartcampus.backend.model.Circular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CircularRepository extends JpaRepository<Circular, Long> {
    List<Circular> findByOrderByPostedDateDesc();
}