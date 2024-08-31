package com.wap.wabi.student.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {
    @Id
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
