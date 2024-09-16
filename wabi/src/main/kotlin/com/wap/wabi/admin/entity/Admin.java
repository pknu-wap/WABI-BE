package com.wap.wabi.admin.entity;

import com.wap.wabi.admin.entity.Enum.AdminStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminNumber;
    private String id;
    private String password;
    private AdminStatus status;

    public Admin() {
    }

    private Admin(builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.status = builder.status;
    }


    public static class builder {
        private String id;
        private String password;
        private AdminStatus status;

        public builder id(String id) {
            this.id = id;
            return this;
        }

        public builder password(String password) {
            this.password = password;
            return this;
        }

        public builder status(AdminStatus status) {
            this.status = status;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}
