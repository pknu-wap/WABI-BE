package com.wap.wabi.event.entity;

import com.wap.wabi.band.entity.Band;
import com.wap.wabi.event.entity.Enum.EventStudentStatus;
import com.wap.wabi.student.entity.Student;
import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class EventStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    private Band band;
    private String club;
    @Enumerated(EnumType.STRING)
    private EventStudentStatus status;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime checkedInAt;

    private EventStudent(builder builder) {
        this.event = builder.event;
        this.student = builder.student;
        this.band = builder.band;
        this.club = builder.club;
        this.status = builder.status;
        this.updatedAt = builder.updatedAt;
        this.checkedInAt = builder.checkedInAt;
    }

    public static class builder {
        private Event event;
        private Student student;
        private Band band;
        private String club;
        private EventStudentStatus status = EventStudentStatus.NOT_CHECK_IN;
        private LocalDateTime updatedAt = LocalDateTime.now();
        private LocalDateTime checkedInAt = null;

        public builder event(Event event) {
            this.event = event;
            return this;
        }

        public builder student(Student student) {
            this.student = student;
            return this;
        }

        public builder band(Band band) {
            this.band = band;
            return this;
        }

        public builder club(String club) {
            this.club = club;
            return this;
        }

        public builder status(EventStudentStatus status) {
            this.status = status;
            return this;
        }

        public builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public builder checkedInAt(LocalDateTime checkedInAt) {
            this.checkedInAt = checkedInAt;
            return this;
        }

        public EventStudent build() {
            return new EventStudent(this);
        }
    }

    public EventStudent() {
    }

    public void checkIn() {
        this.status = EventStudentStatus.CHECK_IN;
        this.checkedInAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Student getStudent() {
        return student;
    }

    public EventStudentStatus getStatus() {
        return status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

}
