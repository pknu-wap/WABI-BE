package com.wap.wabi.event.repository;

import com.wap.wabi.event.entity.Event;
import com.wap.wabi.event.entity.EventStudent;
import com.wap.wabi.event.entity.EventStudentBandName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventStudentBandNameRepository extends JpaRepository<EventStudentBandName, Long> {
    List<EventStudentBandName> findAllByEventStudent(EventStudent eventStudent);

    void deleteByEventStudent(EventStudent eventStudent);
}
