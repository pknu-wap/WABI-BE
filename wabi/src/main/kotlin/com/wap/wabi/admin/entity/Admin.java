package com.wap.wabi.admin.entity;

import com.wap.wabi.admin.entity.Enum.AdminStatus;
import com.wap.wabi.common.utils.StringUtils;
import com.wap.wabi.exception.ErrorCode;
import com.wap.wabi.exception.RestApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminNumber;
    @NotNull
    private String id;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private AdminStatus status;

    public Admin() {
    }

    private Admin(builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.name = builder.name;
        this.status = builder.status;
    }

    public static class builder {
        private String id;
        private String password;
        private String name;
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

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (!isCorrectId() || !isCorrectPassword() || !isCorrectName()) {
            throw new RestApiException(ErrorCode.BAD_REQUEST_ADMIN);
        }
    }

    private boolean isCorrectId() {
        return !id.isBlank() &&
                StringUtils.checkLength(id, 4, 10) &&
                StringUtils.hasOnlySmallLetterOrNumber(id);
    }

    private boolean isCorrectPassword() {
        return !password.isBlank() &&
                StringUtils.checkLength(password, 8, 15) &&
                StringUtils.hasOnlyAllowedSpecialCharacters(password, "~!@#");
    }

    private boolean isCorrectName() {
        return !name.isBlank() &&
                StringUtils.checkLength(name, 2, 10) &&
                StringUtils.hasOnlyAlphabetOrNumber(name);
    }
}
