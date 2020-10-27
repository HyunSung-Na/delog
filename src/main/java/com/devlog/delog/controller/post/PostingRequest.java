package com.devlog.delog.controller.post;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter @Setter
public class PostingRequest {

    private String title;

    private String contents;

    public PostingRequest() {}

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("contents", contents)
                .toString();
    }
}
