package com.wap.wabi.band.repository;

import com.wap.wabi.band.entity.Band;
import com.wap.wabi.band.entity.BandStudent;
import com.wap.wabi.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BandStudentRepository extends JpaRepository<BandStudent, Long> {
    List<BandStudent> findAllByBand(Band band);

    Optional<BandStudent> findByClubAndStudent(String club, Student student);
}
