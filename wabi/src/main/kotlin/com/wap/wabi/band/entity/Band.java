package com.wap.wabi.band.entity;

import com.wap.wabi.band.payload.request.BandUpdateRequest;
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
    private String bandMemo;

    private Band(builder builder) {
        this.adminId = builder.adminId;
        this.bandName = builder.bandName;
        this.bandMemo = builder.bandMemo;
    }

    public static class builder {
        private Long adminId;
        private String bandName;
        private String bandMemo;

        public builder adminId(Long adminId) {
            this.adminId = adminId;
            return this;
        }

        public builder bandName(String bandName) {
            this.bandName = bandName;
            return this;
        }

        public builder bandMemo(String bandMemo) {
            this.bandMemo = bandMemo;
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

    public String getBandMemo() {
        return bandMemo;
    }

    public void update(BandUpdateRequest request) {
        this.bandName = request.getBandName();
    }
}
