package com.wap.wabi.band.entity;

import com.wap.wabi.student.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class BandStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Band band;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    private String club;
    private String position;
    private LocalDate joinDate;
    private String college;
    private String major;
    private String tel;
    private String academicStatus;

    public BandStudent(Band band, Student student, String club, String position, LocalDate joinDate, String college, String major, String tel, String academicStatus) {
        this.band = band;
        this.student = student;
        this.club = club;
        this.position = position;
        this.joinDate = joinDate;
        this.college = college;
        this.major = major;
        this.tel = tel;
        this.academicStatus = academicStatus;
    }

    protected BandStudent() {

    }

    public Long getId() {
        return id;
    }

    public Band getBand() {
        return band;
    }

    public Student getStudent() {
        return student;
    }

    public String getClub() {
        return club;
    }

    public String getPosition() {
        return position;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public String getCollege() {
        return college;
    }

    public String getMajor() {
        return major;
    }

    public String getTel() {
        return tel;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }
}
