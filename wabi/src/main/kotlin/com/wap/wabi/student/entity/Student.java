package com.wap.wabi.student.entity;

import com.wap.wabi.exception.GlobalErrorCode;
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

    private Student(builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static class builder {
        private String id;
        private String name;

        public builder id(@NotNull String id) {
            this.id = id;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    protected Student() {
    }

    @NotNull
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
            throw new RestApiException(GlobalErrorCode.BAD_REQUEST_STUDENT_ID);
        }
    }
}
