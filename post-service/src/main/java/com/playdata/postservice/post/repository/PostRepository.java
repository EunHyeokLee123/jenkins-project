package com.playdata.postservice.post.repository;


import com.playdata.postservice.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 userId를 가진 Post 전체 조회
    List<Post> findByUserId(Long userId);
    
    // 특정 courseId를 가진 Post 전체 조회
    List<Post> findByProductId(Long productId);
}
