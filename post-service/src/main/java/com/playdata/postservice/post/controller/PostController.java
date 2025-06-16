package com.playdata.postservice.post.controller;

import com.playdata.postservice.common.auth.TokenUserInfo;
import com.playdata.postservice.common.dto.CommonErrorDto;
import com.playdata.postservice.common.dto.CommonResDto;
import com.playdata.postservice.post.dto.*;
import com.playdata.postservice.post.entity.Comment;
import com.playdata.postservice.post.entity.Post;
import com.playdata.postservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    // 질문 생성, 확인 완료
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestBody PostSaveReqDto dto) {

        Post post = postService.createPost(dto, userInfo);

        PostResDto postDto = post.fromEntity();

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "질문 등록 성공", postDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 답변 생성, 확인 완료
    // 다만 추후에 다른 서비스와의 통신 과정을 확인해야함.
    @PostMapping("/comment/create")
    public ResponseEntity<?> createCommnet(@AuthenticationPrincipal TokenUserInfo userInfo,
                        @RequestBody CommentSaveReqDto dto) {

        Comment comment = postService.createComment(dto, userInfo);

        CommentResDto commentResDto = CommentResDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .userId(comment.getUserId())
                .build();

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "답변 등록 성공", commentResDto);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 답변 삭제(회원일 때), 확인완료
    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @RequestParam("id") Long commentId){
        boolean isDeleted = postService.deleteCommentUser(commentId, userInfo);
        CommonResDto resDto = new CommonResDto();
        if(isDeleted){
            resDto.setResult(true);
            resDto.setStatusCode(HttpStatus.OK.value());
            resDto.setStatusMessage("댓글이 정상적으로 삭제됨. (회원용)");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        }
        else{
            resDto.setResult(false);
            resDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            resDto.setStatusMessage("댓글이 삭제되지 않음. 비정상적.id가 다른듯. (회원용)");
            return new ResponseEntity<>(resDto, HttpStatus.UNAUTHORIZED);
        }

    }
    
    // 답변 삭제(관리자용), 확인 완료
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/comment/deleteAdmin")
    public ResponseEntity<?> deleteCommentAdmin(@RequestParam("id") Long commentId){

        boolean isDeleted = postService.deleteCommentAdmin(commentId);
        CommonResDto resDto = new CommonResDto();
        if(isDeleted){
            resDto.setResult(true);
            resDto.setStatusCode(HttpStatus.OK.value());
            resDto.setStatusMessage("댓글이 정상적으로 삭제됨. (관리자용)");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        }
        else{

            resDto.setResult(false);
            resDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            resDto.setStatusMessage("댓글이 삭제되지 않음. 비정상적.id가 다른듯. (관리자용)");
            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }

    }

    // 마이페이지에서 본인의 질문 조회, 확인 완료
    @GetMapping("/myquestions")
    public ResponseEntity<?> getMyQuestions(@AuthenticationPrincipal TokenUserInfo userInfo){

        List<PostCoLengthDto> postResDtoList = postService.getMyAllQuestions(userInfo);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, userInfo.getEmail()
                + "님의 질문 정보", postResDtoList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 강의 클릭 시, 하단의 질문 리스트 렌더링을 위한 메소드
    @GetMapping("/list")
    public ResponseEntity<?> getPostsByCourse(@RequestParam("id") Long courseId){

        List<PostCoLengthDto> posts = postService.getAllPostOfCourse(courseId);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK,
                "해당 강의의 모든 질문 찾음!", posts);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 게시물 삭제 로직 (회원용), 확인 완료
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePostUser(@AuthenticationPrincipal TokenUserInfo userInfo,
                                            @RequestParam("id") Long postId){

        boolean isDeleted = postService.deletePostUser(userInfo, postId);
        if(!isDeleted){
            CommonErrorDto errorDto
                    = new CommonErrorDto(HttpStatus.UNAUTHORIZED, "해당 게시물의 작성자가 아닙니다.");
            return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
        }
        CommonResDto resDto = new CommonResDto(HttpStatus.OK,
                "정상적으로 게시물이 삭제되었습니다.", postId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 게시물 삭제 (관리자용), 확인 완료
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteadmin")
    public ResponseEntity<?> deletePostAdmin(@RequestParam("id") Long postId){
        postService.deletePostAdmin(postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 회원 탈퇴 시 해당 회원의 모든 게시물과 댓글을 삭제하는 로직 -> 확인완료
    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deletePostUser(@RequestParam("id") Long userId){

        postService.removeUserAllPost(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/comment/find")
    public ResponseEntity<?> getPostsComment(@RequestParam("id") Long postId){

        List<CommentResDto> commentByPostId = postService.findCommentByPostId(postId);

        CommonResDto resDto =
                new CommonResDto(HttpStatus.OK, "해당 질문의 모든 답변 찾음.", commentByPostId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyPost(@AuthenticationPrincipal TokenUserInfo userInfo,
                                        @RequestBody PostModiReqDto reqDto){

        PostResDto resDto = postService.modifyPost(reqDto, userInfo);
        if(resDto == null){
            return new ResponseEntity(false, HttpStatus.NOT_MODIFIED);
        }
        CommonResDto dto = new CommonResDto(HttpStatus.OK, "해당" + reqDto.getPostId() + "post가 잘 수정됨", resDto);

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

}
