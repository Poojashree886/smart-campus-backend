package com.smartcampus.backend.repository;

import com.smartcampus.backend.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByUserId(Long userId);
    Optional<Parent> findByEmail(String email);
}