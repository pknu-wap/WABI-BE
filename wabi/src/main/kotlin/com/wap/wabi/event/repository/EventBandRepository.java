package com.wap.wabi.event.repository;

import com.wap.wabi.event.entity.EventBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventBandRepository extends JpaRepository<EventBand, Long> {
}
