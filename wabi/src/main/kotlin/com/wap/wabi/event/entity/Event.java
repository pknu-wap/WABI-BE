package com.wap.wabi.event.entity;

import com.wap.wabi.event.payload.request.EventUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long adminId;
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int eventStudentMaxCount;

    private Event(builder builder) {
        this.adminId = builder.adminId;
        this.name = builder.name;
        this.startAt = builder.startAt;
        this.endAt = builder.endAt;
        this.eventStudentMaxCount = builder.eventStudentMaxCount;
    }

    public static class builder {
        private Long adminId;
        private String name;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private int eventStudentMaxCount;

        public builder adminId(Long adminId) {
            this.adminId = adminId;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public builder startAt(LocalDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public builder endAt(LocalDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public builder eventStudentMaxCount(int eventStudentMaxCount) {
            this.eventStudentMaxCount = eventStudentMaxCount;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }

    protected Event() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void update(EventUpdateRequest request) throws IllegalAccessException {
        validateId(request.getEventId());
        this.name = request.getEventName();
        this.startAt = request.getStartAt();
        this.endAt = request.getEndAt();
        this.eventStudentMaxCount = request.getEventStudentMaxCount();
    }

    private void validateId(Long id) throws IllegalAccessException {
        if (this.id != id) {
            throw new IllegalAccessException("Event ID does not match.");
        }
    }
}
