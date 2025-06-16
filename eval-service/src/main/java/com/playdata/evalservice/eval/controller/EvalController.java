package com.playdata.evalservice.eval.controller;

import com.playdata.evalservice.common.auth.TokenUserInfo;
import com.playdata.evalservice.common.dto.CommonResDto;
import com.playdata.evalservice.eval.dto.EvalModiReqDto;
import com.playdata.evalservice.eval.dto.EvalRateLenDto;
import com.playdata.evalservice.eval.dto.EvalResDto;
import com.playdata.evalservice.eval.dto.EvalSaveReqDto;
import com.playdata.evalservice.eval.service.EvalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eval")
@RequiredArgsConstructor
@Slf4j
public class EvalController {

    private final EvalService evalService;

    // 평가 생성 가능 여부 확인 로직 -> 평가 등록 버튼 클릭 시 이 메소드가 요청될 것임.
    @GetMapping("/can-create/{id}")
    public ResponseEntity<?> canEval(@AuthenticationPrincipal TokenUserInfo userInfo, @PathVariable(name = "id") Long evalId) {
        boolean canCreate = evalService.canEvalCourse(userInfo, evalId);

        if(canCreate) {
            return new ResponseEntity<>(canCreate, HttpStatus.ACCEPTED);
        }
        else{
            return new ResponseEntity<>(canCreate, HttpStatus.FORBIDDEN);
        }
    }


    // 평가 생성
    @PostMapping("/create")
    public ResponseEntity<?> createEvaluation(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody EvalSaveReqDto reqDto){

        EvalResDto resDto = evalService.createEval(userInfo, reqDto);
        
        // 이미 평가를 진행한 경우
        if(resDto == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        CommonResDto<EvalResDto> dto =
                new CommonResDto<>(HttpStatus.CREATED, "평가 생성됨!", resDto);
        
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // 평가 삭제
    // 마이페이지, 강의 상세 정보에서 나오는 나의 평가에서만 삭제버튼이 존재할 것이기 때문에,
    // 딱히, 확인 로직을 강하게 하지는 않겠음.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvaluation(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @PathVariable(name = "id") Long evalId){

        boolean isDeleted = evalService.deleteEval(userInfo, evalId);

        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    // 평가 수정
    @PostMapping("/modify")
    public ResponseEntity<?> modifyEvaluation(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody EvalModiReqDto modiDto){

        EvalResDto evalResDto = evalService.modifyEval(userInfo, modiDto);

        if(evalResDto == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        CommonResDto<EvalResDto> commonResDto = new CommonResDto<>(HttpStatus.ACCEPTED, "평가 수정이 성공적으로 이루어졌습니다.", evalResDto);

        return new ResponseEntity<>(commonResDto, HttpStatus.ACCEPTED);

    }

    // 강의의 모든 평가 조회 시 내 평가 조회
    @GetMapping("/my-eval/{id}")
    public ResponseEntity<?> myEval(@AuthenticationPrincipal TokenUserInfo userInfo,
                                    @PathVariable(name="id") Long prodId){

        EvalResDto evalResDto = evalService.findProdMyEval(userInfo, prodId);

        CommonResDto<EvalResDto> commonResDto = new CommonResDto<>(HttpStatus.OK, "강의의 내 평가 조회", evalResDto);

        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    // 마이페이지에서 평가 조회

    @GetMapping("/my-all-evals")
    public ResponseEntity<?> findMyAllEval(@AuthenticationPrincipal TokenUserInfo userInfo){

        List<EvalResDto> myEvals =
                evalService.findMyEval(userInfo);

        CommonResDto<List<EvalResDto>> resDto = new CommonResDto<>(HttpStatus.OK, "유저의 모든 평가 찾음", myEvals);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 강의 디테일(강의 조회 시)에 대한 모든 평가 조회
    // 단일 강의의 모든 평가 조회
    // token 필요 없음
    @GetMapping("/course-all-eval/{id}")
    public ResponseEntity<?> findCourseAllEval(@PathVariable(name = "id") Long prodId){

        List<EvalResDto> foundEvals = evalService.findProdAllEval(prodId);

        CommonResDto<List<EvalResDto>> resDto = new CommonResDto<>(HttpStatus.OK, "해당 강의의 모든 평가 찾음", foundEvals);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 강의 상세 페이지에 평점을 보내줄 메소드
    // token 필요 없음
    @GetMapping("/eval-rating/{id}")
    public ResponseEntity<?> findOneCourseRating(@PathVariable(name = "id") Long productId){

        double rating = evalService.findOneCourseRating(productId);

        CommonResDto<Double> resDto = new CommonResDto<>(HttpStatus.OK, "해당 강의의 평점의 평균을 구함", rating);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


    // 강의 리스트가 화면단에 출력될 때, 평점을 보여주기 위한 강의의 전체 평점 평균 조회
    // MainPage에서 course/all의 리턴값을 변형해서 prodId만을 담은 리스트를 받자.
    // 이건 course-service에서 fegin으로 보내는 것이 좋을 듯
    // Map<강의 아이디, 평점>    --> 이건  프론트 단에서 보내는 요청을 받음

    // token 필요 없음   --> 이건 전체 용도로 하자.
    @PostMapping("/course-eval-rating")
    public ResponseEntity<?> findCoursesAverageRating(@RequestBody List<Long> prodIdList){

        Map<Long, Double> ratings = evalService.findCourseRating(prodIdList);

        CommonResDto<Map<Long, Double>> resDto = new CommonResDto<>(HttpStatus.OK, "해당 강의들의 모든 평균 평점을 찾음", ratings);

        ratings.forEach((k,v)->{
            log.info("key: {}, value: {}", k, v.toString());
        });

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 이건 course-service에서 feign으로 보낸다는 가정 하에 작성하는 로직
    // 위의 메소드와 둘 중 하나만 사용할 것
    @PostMapping("/course-eval-rating-feign")
    public CommonResDto<Map<Long, Double>> findCoursesRatingFeign(@RequestBody List<Long> prodIdList) {
        Map<Long, Double> ratingMap = evalService.findCourseRating(prodIdList);
        return new CommonResDto<>(HttpStatus.OK,"해당 강의들의 모든 평균 평점을 찾음",ratingMap );
    }

    // 평가 등록 및 수정 시 평가 개수와 평점의 평균을 최신화하는 로직
    // token 필요 없음.
    @GetMapping("/update-info/{id}")
    public ResponseEntity<?> updateEvalInfo(@PathVariable(name = "id") Long prodId){

        EvalRateLenDto evalInfo = evalService.updateEvalInfo(prodId);

        CommonResDto<EvalRateLenDto> resDto = new CommonResDto<>(HttpStatus.OK, "해당 강의의 평점과 갯수", evalInfo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
