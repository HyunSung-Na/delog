package com.devlog.delog.service;

import com.devlog.delog.controller.post.PostingRequest;
import com.devlog.delog.domain.Account;
import com.devlog.delog.domain.Likes;
import com.devlog.delog.domain.Post;
import com.devlog.delog.repository.PostLikeRepository;
import com.devlog.delog.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    public PostService(PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    public Post write(PostingRequest postingRequest, Account writer) {
        Post newPost = Post.builder()
                .contents(postingRequest.getContents())
                .commentCount(0)
                .title(postingRequest.getTitle())
                .likes(0)
                .account(writer)
                .likeOfMe(false)
                .build();
        return postRepository.save(newPost);
    }


    public Optional<Post> like(Long postId, Account account) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(account, "account must be provided.");
        Likes likes = Likes.builder()
                .post(postRepository.getOne(postId))
                .account(account)
                .createAt(LocalDateTime.now())
                .build();

        return postRepository.findById(postId).map(post -> {
            if (!post.isLikeOfMe()) {
                post.incrementAndGetLikes();
                postLikeRepository.save(likes);
                postRepository.save(post);
            }
            return post;
        });
    }

    @Transactional(readOnly = true)
    public Optional<Post> findPost(Long postId) {
        checkNotNull(postId, "postId must be provided.");
        return postRepository.findById(postId);
    }


    @Transactional(readOnly = true)
    public List<Post> findAll(int offset, int limit) {

        if (offset < 0)
            offset = 0;
        if (limit < 1 || limit > 5)
            limit = 5;

        PageRequest pageRequest = PageRequest.of(offset, limit);
        return postRepository.findAll(pageRequest).getContent();
    }

}
