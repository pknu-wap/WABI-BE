package com.wap.wabi.event.entity;

import jakarta.persistence.*;

@Entity
public class EventStudentBandName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private EventStudent eventStudent;
    private String bandName;

    private EventStudentBandName(builder builder) {
        this.eventStudent = builder.eventStudent;
        this.bandName = builder.bandName;
    }

    public static class builder {
        private EventStudent eventStudent;
        private String bandName;

        public builder eventStudent(EventStudent eventStudent) {
            this.eventStudent = eventStudent;
            return this;
        }

        public builder bandName(String bandName) {
            this.bandName = bandName;
            return this;
        }

        public EventStudentBandName build() {
            return new EventStudentBandName(this);
        }
    }

    protected EventStudentBandName() {
    }

    public Long getId() {
        return id;
    }

    public EventStudent getEventStudent() {
        return eventStudent;
    }

    public String getBandName() {
        return bandName;
    }
}
