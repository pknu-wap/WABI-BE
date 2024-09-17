package com.wap.wabi.auth.admin.entity;

import com.wap.wabi.auth.admin.entity.Enum.AdminStatus;
import com.wap.wabi.common.utils.StringUtils;
import com.wap.wabi.exception.ErrorCode;
import com.wap.wabi.exception.RestApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String password;
    @NotNull
    private String email;

    private AdminStatus status;

    public Admin() {
    }

    private Admin(builder builder) {
        this.name = builder.name;
        this.password = builder.password;
        this.email = builder.email;
        this.status = builder.status;
    }

    public static class builder {
        private String name;
        private String password;
        private String email;
        private AdminStatus status;

        public builder password(String password) {
            this.password = password;
            return this;
        }

        public builder status(AdminStatus status) {
            this.status = status;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }
        public builder email(String email) {
            this.email = email;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (!isCorrectName() || !isCorrectPassword()) {
            throw new RestApiException(ErrorCode.BAD_REQUEST_ADMIN);
        }
    }

    private boolean isCorrectName() {
        return !name.isBlank() &&
                StringUtils.checkLength(name, 4, 10) &&
                StringUtils.hasOnlySmallLetterOrNumber(name);
    }

    private boolean isCorrectPassword() {
        return !password.isBlank() &&
                StringUtils.checkLength(password, 8, 15) &&
                StringUtils.hasOnlyAllowedSpecialCharacters(password, "~!@#");
    }
}
