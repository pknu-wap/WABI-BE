package com.wap.wabi.student.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Student {
    @Id
    private String id;
    private String name;

}
