package com.devlog.delog.repository;

import com.devlog.delog.domain.Account;
import com.devlog.delog.domain.Likes;
import com.devlog.delog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByAccountAndPost(Account account, Post post);
}
