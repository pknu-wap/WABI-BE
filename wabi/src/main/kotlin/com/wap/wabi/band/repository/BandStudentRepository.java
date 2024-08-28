package com.wap.wabi.band.repository;

import com.wap.wabi.band.entity.Band;
import com.wap.wabi.band.entity.BandStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BandStudentRepository extends JpaRepository<BandStudent, Long> {
    List<BandStudent> findAllByBand(Band band);
}
