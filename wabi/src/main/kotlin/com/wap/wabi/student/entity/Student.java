package com.wap.wabi.student.entity;

import com.wap.wabi.exception.ErrorCode;
import com.wap.wabi.exception.RestApiException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.jetbrains.annotations.NotNull;

@Entity
public class Student {
    @Id
    @NotNull
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

    @PrePersist
    @PreUpdate
    private void validate() {
        if (id.isBlank()) {
            throw new RestApiException(ErrorCode.BAD_REQUEST_STUDENT_ID);
        }
    }
}
