package com.wap.wabi.band.repository;

import com.wap.wabi.band.entity.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandRepository extends JpaRepository<Band, Long> {
    List<Band> findAllByAdminId(Long adminId);
}
