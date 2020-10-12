package com.devlog.delog.domain;

import lombok.*;
import org.springframework.context.annotation.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
@Entity
@Table
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String password;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String roles;

    @Column
    private LocalDateTime joinedAt;

    @Column
    private String emailCheckToken;

    private boolean emailVerified;

    @Column
    private LocalDateTime emailCheckTokenGeneratedAt;

    public Account(Long id, String password, String username, String email, String roles, LocalDateTime joinedAt, String emailCheckToken, boolean emailVerified, LocalDateTime emailCheckTokenGeneratedAt) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.joinedAt = joinedAt;
        this.emailCheckToken = emailCheckToken;
        this.emailVerified = emailVerified;
        this.emailCheckTokenGeneratedAt = emailCheckTokenGeneratedAt;
    }

    public Account() {

    }

    public Account(Account account) {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }


    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

}
