package com.devlog.delog.controller.post;

import com.devlog.delog.controller.ApiResult;
import com.devlog.delog.domain.Account;
import com.devlog.delog.domain.Post;
import com.devlog.delog.error.NotFoundException;
import com.devlog.delog.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("info/{postId}/{userId}")
    public ApiResult<PostDto> postInfo(@PathVariable Long postId, @PathVariable Long userId) {

        return ApiResult.OK(
                postService.findPost(postId)
                .map(PostDto::new)
                .orElseThrow(() -> new NotFoundException(Post.class, postId)));
    }

    @GetMapping("list")
    public ApiResult<List<PostDto>> posts(int page, int size) {
        return ApiResult.OK(
                postService.findAll(page, size).stream()
                .map(PostDto::new)
                .collect(Collectors.toList())
        );
    }

    @PostMapping("write")
    public ApiResult<PostDto> posting(PostingRequest postingRequest, @AuthenticationPrincipal Account account) {
        Post newPost = postService.write(postingRequest, account);

        return ApiResult.OK(new PostDto(newPost));
    }

    @PostMapping("like/{postId}")
    public ApiResult<PostDto> likes(@PathVariable Long postId, @AuthenticationPrincipal Account account) {
        return ApiResult.OK(
                postService.like(postId, account)
                .map(PostDto::new)
                .orElseThrow(() -> new NotFoundException(Post.class, postId))
        );
    }
}
