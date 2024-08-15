package com.wap.wabi.event.repository;

import com.wap.wabi.event.entity.Event;
import com.wap.wabi.event.entity.EventStudent;
import com.wap.wabi.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventStudentRepository extends JpaRepository<EventStudent, Long> {
    Optional<EventStudent> findByStudentAndEvent(Student student, Event event);
}
