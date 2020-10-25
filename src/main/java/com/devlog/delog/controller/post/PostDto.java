package com.devlog.delog.controller.post;

import com.devlog.delog.domain.Post;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter @Setter
public class PostDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

    private int like;

    private int commentCount;

    private boolean likeOfMe;

    private LocalDateTime createAt;

    private String writer;

    public PostDto (Post source) {
        copyProperties(source, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("contents", contents)
                .append("like", like)
                .append("commentCount", commentCount)
                .append("likeOfMe", likeOfMe)
                .append("createAt", createAt)
                .append("writer", writer)
                .toString();
    }
}
