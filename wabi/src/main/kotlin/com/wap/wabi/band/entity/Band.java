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
