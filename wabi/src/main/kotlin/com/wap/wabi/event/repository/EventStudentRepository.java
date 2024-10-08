package com.wap.wabi.event.repository;

import com.wap.wabi.event.entity.Enum.EventStudentStatus;
import com.wap.wabi.event.entity.Event;
import com.wap.wabi.event.entity.EventStudent;
import com.wap.wabi.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventStudentRepository extends JpaRepository<EventStudent, Long> {
    Optional<EventStudent> findByStudentAndEvent(Student student, Event event);

    @Query("SELECT es FROM EventStudent es WHERE es.event=:event ORDER BY es.checkedInAt DESC")
    List<EventStudent> findAllByEvent(@Param("event") Event event);

    List<EventStudent> findAllByEventAndStatus(Event event, EventStudentStatus status);

    @Query("SELECT COUNT(*) FROM EventStudent es WHERE es.status=:status AND es.event=:event")
    int getEventStudentStatusCount(@Param("event") Event event, @Param("status") EventStudentStatus status);

    void deleteByEvent(Event event);
}
