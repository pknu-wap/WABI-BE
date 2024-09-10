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
        this.name = builder.name;
        this.startAt = builder.startAt;
        this.endAt = builder.endAt;
    }

    public static class builder {
        private String name;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

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
