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

    private BandStudent(builder builder) {
        this.band = builder.band;
        this.student = builder.student;
        this.club = builder.club;
        this.position = builder.position;
        this.joinDate = builder.joinDate;
        this.college = builder.college;
        this.major = builder.major;
        this.tel = builder.tel;
        this.academicStatus = builder.academicStatus;
    }

    public static class builder {
        private Band band;
        private Student student;
        private String club;
        private String position;
        private LocalDate joinDate;
        private String college;
        private String major;
        private String tel;
        private String academicStatus;

        public builder band(Band band) {
            this.band = band;
            return this;
        }

        public builder student(Student student) {
            this.student = student;
            return this;
        }

        public builder club(String club) {
            this.club = club;
            return this;
        }

        public builder position(String position) {
            this.position = position;
            return this;
        }

        public builder joinDate(LocalDate joinDate) {
            this.joinDate = joinDate;
            return this;
        }

        public builder college(String college) {
            this.college = college;
            return this;
        }

        public builder major(String major) {
            this.major = major;
            return this;
        }

        public builder tel(String tel) {
            this.tel = tel;
            return this;
        }

        public builder academicStatus(String academicStatus) {
            this.academicStatus = academicStatus;
            return this;
        }

        public BandStudent build() {
            return new BandStudent(this);
        }
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
