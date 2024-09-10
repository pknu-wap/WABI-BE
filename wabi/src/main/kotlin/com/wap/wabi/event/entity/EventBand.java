package com.wap.wabi.event.entity;

import com.wap.wabi.band.entity.Band;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EventBand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private Band band;

    private EventBand(builder builder) {
        this.event = builder.event;
        this.band = builder.band;
    }

    public static class builder {
        private Event event;
        private Band band;

        public builder event(Event event) {
            this.event = event;
            return this;
        }

        public builder band(Band band) {
            this.band = band;
            return this;
        }

        public EventBand build() {
            return new EventBand(this);
        }
    }

    protected EventBand() {
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Band getBand() {
        return band;
    }
}
