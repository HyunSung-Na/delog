package com.devlog.delog.controller.post;

import com.devlog.delog.domain.Account;
import com.devlog.delog.domain.Comment;
import com.devlog.delog.domain.Post;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CommentRequest {

    private String contents;

    protected CommentRequest() {}

    public String getContents() {
        return contents;
    }

    public Comment newComment(String contents, String writer, Post post, Account account) {
        return new Comment(contents, writer, post, account);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("contents", contents)
                .toString();
    }

}
