package com.devlog.delog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@EqualsAndHashCode(of = "id")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreationTimestamp
    private LocalDateTime createAt;

    @ManyToOne
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JsonIgnore
    private Post post;

    public Likes() {

    }

    public Likes(Long id, LocalDateTime createAt, Account account, Post post) {
        this.id = id;
        this.createAt = createAt;
        this.account = account;
        this.post = post;
    }
}
