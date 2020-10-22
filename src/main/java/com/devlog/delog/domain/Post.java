package com.devlog.delog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Slf4j
@EqualsAndHashCode(of = "id")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private int likes;

    @Column
    private int commentCount;

    @Column
    private boolean likeOfMe;

    @Column
    private LocalDateTime createAt;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Likes> like = new ArrayList<>();

    public Post() {

    }

    public Post(String title, String writer, String contents, Account account) {
        this(null, title, contents, 0, 0, false, null, writer, account);
    }

    public Post(Long id, String title, String contents, int likes, int commentCount, boolean likeOfMe, LocalDateTime createAt, String writer, Account account) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.likes = likes;
        this.commentCount = commentCount;
        this.likeOfMe = likeOfMe;
        this.createAt = createAt;
        this.writer = writer;
        this.account = account;
    }

    public void setAccount(Account account) {
        if (this.account != null) {
            this.account.getBoards().remove(this);
        }
        this.account = account;
        account.getBoards().add(this);
    }

    @Transient
    public int commentCount() {
        return comments != null ? comments.size() : 0;
    }

    public boolean isLikesOfMe() {
        return likeOfMe;
    }

    public int incrementAndGetLikes() {
        likeOfMe = true;
        return ++likes;
    }

    public int incrementAndGetComments() {
        return ++commentCount;
    }
}
