package com.wap.wabi.event.entity;

import com.wap.wabi.event.entity.Enum.EventStudentStatus;
import com.wap.wabi.student.entity.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class EventStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    private String group;

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
    public String getGroup() {return group;}

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
