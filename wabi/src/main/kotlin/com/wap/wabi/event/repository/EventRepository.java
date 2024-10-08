package com.wap.wabi.event.repository;

import com.wap.wabi.band.entity.Band;
import com.wap.wabi.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByAdminId(Long id);
}
