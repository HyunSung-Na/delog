package com.devlog.delog.repository;

import com.devlog.delog.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<Likes, Long> {
}
