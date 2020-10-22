package com.devlog.delog.controller.post;

import com.devlog.delog.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;


@Getter @Setter
public class CommentDto {

    private Long id;

    @NotEmpty
    private String contents;

    private String writer;

    private LocalDateTime createAt;

    public CommentDto(Comment source) {
        copyProperties(source, this);

        this.writer = source.getWriter();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("contents", contents)
                .append("writer", writer)
                .append("createAt", createAt)
                .toString();
    }
}
