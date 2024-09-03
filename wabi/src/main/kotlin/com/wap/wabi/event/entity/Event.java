package com.wap.wabi.event.entity;

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
    private String name;
    private LocalDateTime startAt;

    private Event(builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.startAt = builder.startAt;
        this.endAt = builder.endAt;
    }

    public static class builder {
        private Long id;
        private String name;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        public builder setId(Long id) {
            this.id = id;
            return this;
        }

        public builder setName(String name) {
            this.name = name;
            return this;
        }

        public builder setStartAt(LocalDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public builder setEndAt(LocalDateTime endAt) {
            this.endAt = endAt;
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

    private LocalDateTime endAt;
}
