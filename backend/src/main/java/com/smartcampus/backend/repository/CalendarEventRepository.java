package com.smartcampus.backend.repository;

import com.smartcampus.backend.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByStartDateBetween(LocalDate start, LocalDate end);
    List<CalendarEvent> findByType(String type);
    List<CalendarEvent> findByOrderByStartDateAsc();
}