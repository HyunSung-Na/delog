package com.devlog.delog.service;

import com.devlog.delog.controller.post.PostingRequest;
import com.devlog.delog.domain.Account;
import com.devlog.delog.domain.Likes;
import com.devlog.delog.domain.Post;
import com.devlog.delog.repository.AccountRepository;
import com.devlog.delog.repository.PostLikeRepository;
import com.devlog.delog.repository.PostRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class PostService {

    private final AccountRepository accountRepository;

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    public PostService(AccountRepository accountRepository, PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public Post write(PostingRequest postingRequest, Account writer) {
        Post newPost = Post.builder()
                .contents(postingRequest.getContents())
                .commentCount(0)
                .writer(writer.getUsername())
                .title(postingRequest.getTitle())
                .likes(0)
                .account(writer)
                .likeOfMe(false)
                .build();
        return postRepository.save(newPost);
    }


    @Transactional
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
    public Page<Post> findAll(int offset, int limit) {

        if (offset < 0)
            offset = 0;
        if (limit < 1 || limit > 5)
            limit = 5;

        PageRequest pageRequest = PageRequest.of(offset, limit);
        return postRepository.findAll(pageRequest);
    }

}
