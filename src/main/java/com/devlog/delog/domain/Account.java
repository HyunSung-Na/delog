package com.devlog.delog.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Getter
@Entity
@Table
@EqualsAndHashCode(of = "id")
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String password;

    @Column
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Setter
    private List<String> roles = new ArrayList<>();

    @Column
    private LocalDateTime joinedAt;

    @Column
    private String emailCheckToken;

    private boolean emailVerified;

    @Column
    private LocalDateTime emailCheckTokenGeneratedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private final List<Post> boards = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private final List<Likes> like = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public Account(Long id, String password, String username, String email, List<String> roles, LocalDateTime joinedAt, String emailCheckToken, boolean emailVerified, LocalDateTime emailCheckTokenGeneratedAt) {
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
        this.setRoles(Collections.singletonList("ROLE_USER"));
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
