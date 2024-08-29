package com.wap.wabi.event.entity;

import com.wap.wabi.event.entity.Enum.EventStudentStatus;
import com.wap.wabi.student.entity.Student;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class EventStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
    private String band;
    @Enumerated(EnumType.STRING)
    private EventStudentStatus status;
    private LocalDateTime updatedAt;
    private LocalDateTime checkedInAt;

    public void checkIn(){
        this.status = EventStudentStatus.CHECK_IN;
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
