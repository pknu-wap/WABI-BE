package com.wap.wabi.band.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long adminId;
    private String bandName;

    private Band(builder builder) {
        this.id = builder.id;
        this.adminId = builder.adminId;
        this.bandName = builder.bandName;
    }

    public static class builder {
        private Long id;
        private Long adminId;
        private String bandName;

        public builder setId(Long id) {
            this.id = id;
            return this;
        }

        public builder setAdminId(Long adminId) {
            this.adminId = adminId;
            return this;
        }

        public builder setBandName(String bandName) {
            this.bandName = bandName;
            return this;
        }

        public Band build() {
            return new Band(this);
        }
    }

    protected Band() {
    }

    public Long getId() {
        return id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getBandName() {
        return bandName;
    }
}
