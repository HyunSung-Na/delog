package com.devlog.delog.service;

import com.devlog.delog.domain.Comment;
import com.devlog.delog.domain.Post;
import com.devlog.delog.error.NotFoundException;
import com.devlog.delog.repository.CommentRepository;
import com.devlog.delog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class CommentService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment write(Long postId, Comment comment) {
        checkArgument(comment.getPost().equals(postId), "comment.postId must equals postId");
        checkNotNull(comment, "comment must be provided.");

        return findPost(postId)
                .map(post -> {
                    post.incrementAndGetComments();
                    postRepository.save(post);
                    return insert(comment);
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }


    private Optional<Post> findPost(Long postId) {
        checkNotNull(postId, "postId must be provided.");
        return postRepository.findById(postId);
    }

    private Comment insert(Comment comment) {
        return commentRepository.save(comment);
    }
}
