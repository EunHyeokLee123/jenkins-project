package com.playdata.postservice.post.repository;

import com.playdata.postservice.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 postId를 가진 모든 댓글을 리턴 (게시물 삭제 시 댓글 삭제를 위해 사용)
    List<Comment> findByPostId(Long postId);

}
