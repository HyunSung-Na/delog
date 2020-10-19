package com.devlog.delog.controller.account;

import com.devlog.delog.domain.Account;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

public class AccountDto {

    private Long id;

    private String username;

    private String email;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    private List<String> roles;

    public AccountDto(Account source) {
        copyProperties(source, this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailCheckToken() {
        return emailCheckToken;
    }

    public void setEmailCheckToken(String emailCheckToken) {
        this.emailCheckToken = emailCheckToken;
    }

    public LocalDateTime getEmailCheckTokenGeneratedAt() {
        return emailCheckTokenGeneratedAt;
    }

    public void setEmailCheckTokenGeneratedAt(LocalDateTime emailCheckTokenGeneratedAt) {
        this.emailCheckTokenGeneratedAt = emailCheckTokenGeneratedAt;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("username", username)
                .append("email", email)
                .append("emailVerified", emailVerified)
                .append("emailCheckToken", emailCheckToken)
                .append("emailCheckTokenGeneratedAt", emailCheckTokenGeneratedAt)
                .append("joinedAt", joinedAt)
                .append("roles", roles)
                .toString();
    }
}
