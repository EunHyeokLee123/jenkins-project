package com.playdata.postservice.post.service;

import com.playdata.postservice.client.CourseServiceClient;
import com.playdata.postservice.client.UserServiceClient;
import com.playdata.postservice.common.auth.TokenUserInfo;
import com.playdata.postservice.common.dto.CommonResDto;
import com.playdata.postservice.post.dto.*;
import com.playdata.postservice.post.entity.Comment;
import com.playdata.postservice.post.entity.Post;
import com.playdata.postservice.post.entity.PostStatus;
import com.playdata.postservice.post.repository.CommentRepository;
import com.playdata.postservice.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    private final CourseServiceClient courseServiceClient;
    private final UserServiceClient userServiceClient;

    public Post createPost(PostSaveReqDto dto, TokenUserInfo userInfo) {

        Long userId = getUserId(userInfo);

        Post newPost = dto.toEntity(userId);

        return postRepository.save(newPost);

    }

    public Comment createComment(CommentSaveReqDto dto, TokenUserInfo userInfo) {

        Long userId = getUserId(userInfo);
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(()->{
                    return new EntityNotFoundException("없는 포스트입니다.");
                });
        Long postId = post.getId();
        Long productId = post.getProductId();


        CommonResDto<CourseResDto> resDto = courseServiceClient.getIdByCourseId(productId);
        CourseResDto result = resDto.getResult();
        Long teacherId = result.getUserId();
        if(userId == teacherId){
            post.changeStatus(PostStatus.ANSWERED);
            postRepository.save(post);
        }

        Comment newComment = Comment.builder()
                .userId(userId)
                .content(dto.getContent())
                .post(post)
                .build();

        commentRepository.save(newComment);

        return newComment;
    }

    private Long getUserId(TokenUserInfo userInfo) {
        String email = userInfo.getEmail();

        CommonResDto<UserResDto> foundUser = userServiceClient.getIdByEmail(email);
        UserResDto result = foundUser.getResult();
        Long id = result.getId();

        log.info(result.toString());
        return id;

    }

    public boolean deleteCommentUser(Long commentId, TokenUserInfo userInfo) {

        Long userId = getUserId(userInfo);
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(() -> {
            return new EntityNotFoundException("해당 댓글은 없음.");
        });
        if(foundComment.getUserId() != userId){
            return false;
        }
        commentRepository.delete(foundComment);
        return true;
    }

    public boolean deleteCommentAdmin(Long commentId) {

        Optional<Comment> foundComment = commentRepository.findById(commentId);
        if(foundComment.isPresent()){
            commentRepository.delete(foundComment.get());
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<PostCoLengthDto> getMyAllQuestions(TokenUserInfo userInfo) {

        Long userId = getUserId(userInfo);
        List<Post> foundPosts = postRepository.findByUserId(userId);

        List<PostCoLengthDto> list = foundPosts.stream().map((post) -> {
            int size = commentRepository.findByPostId(post.getId()).size();
            PostCoLengthDto dto = new PostCoLengthDto();
            PostCoLengthDto dto1 = dto.onlyAddLength(post, size);
            return dto1;
        }).toList();

        return list;
    }

    public boolean deletePostUser(TokenUserInfo userInfo, Long postId) {

        Long userId = getUserId(userInfo);

        Post foundPost = postRepository.findById(postId).orElseThrow(()
                -> new EntityNotFoundException("해당 질문은 없음!"));

        Long foundUserId = foundPost.getUserId();
        if(foundUserId != userId){
            return false;
        }

        removePostComment(foundPost.getId());
        postRepository.delete(foundPost);
        return true;
    }

    // 게시물 삭제 시 해당 게시물의 모든 댓글도 삭제하는 메소드
    public void removePostComment(Long postId){
        List<Comment> foundComment = commentRepository.findByPostId(postId);

        foundComment.forEach(commentRepository::delete);
    }

    public void deletePostAdmin(Long postId) {

        Post foundPost = postRepository.findById(postId).orElseThrow(()
                -> new EntityNotFoundException("해당 질문은 없음!"));

        removePostComment(foundPost.getId());
        postRepository.delete(foundPost);
    }

    public void removeUserAllPost(Long userId) {

        List<Post> foundUserPost = postRepository.findByUserId(userId);
        foundUserPost.forEach(post -> {
            removePostComment(post.getId());
            postRepository.delete(post);
        });

    }

    @Transactional(readOnly = true)
    public List<PostCoLengthDto> getAllPostOfCourse(Long courseId) {

        List<Post> foundPosts = postRepository.findByProductId(courseId);

        List<PostCoLengthDto> list = foundPosts.stream().map((post) -> {
            int size = commentRepository.findByPostId(post.getId()).size();
            PostCoLengthDto dto = new PostCoLengthDto();
            PostCoLengthDto dto1 = dto.onlyAddLength(post, size);
            return dto1;
        }).toList();

        return list;
    }

    @Transactional(readOnly = true)
    public List<CommentResDto> findCommentByPostId(Long postId) {

        List<Comment> foundComments = commentRepository.findByPostId(postId);

        List<CommentResDto> list = foundComments.stream().map((comment -> {
            return CommentResDto.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .userId(comment.getUserId())
                    .postId(comment.getPost().getId())
                    .build();
        })).toList();

        return list;

    }

    public PostResDto modifyPost(PostModiReqDto reqDto, TokenUserInfo userInfo) {

        Long requestUserId = getUserId(userInfo);
        Post foundPost = postRepository.findById(reqDto.getPostId()).orElseThrow(() -> {
            return new EntityNotFoundException("없는 강의입니다.");
        });

        if(foundPost.getUserId() != requestUserId){
            return null;
        }
        foundPost.updateTitleAndContent(reqDto.getTitle(), reqDto.getContent());


        Post save = postRepository.save(foundPost);

        PostResDto resDto = save.fromEntity();
        return resDto;
    }
}
