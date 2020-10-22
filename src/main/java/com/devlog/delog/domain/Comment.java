package com.devlog.delog.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Slf4j
@Builder
@ToString(exclude = {"post"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int depth;

    private LocalDateTime createDate;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;

    public Comment(String content, String writer, Post post, Account account) {
        this(null, content, 0, null, writer, post, account);
    }


    public Comment(Long id, String content, int depth, LocalDateTime createDate, String writer, Post post) {
        this.id = id;
        this.content = content;
        this.depth = depth;
        this.createDate = createDate;
        this.writer = writer;
        this.post = post;
        this.account = getAccount();
    }

    public Comment() {

    }

    public Comment(Object o, String content, int i, Object o1, String writer, Post post, Account account) {
    }

    public void setPost(Post post) {
        if(this.post != null) {
            this.post.getComments().remove(this);
        }
        this.post = post;
        post.getComments().add(this);
    }

}
