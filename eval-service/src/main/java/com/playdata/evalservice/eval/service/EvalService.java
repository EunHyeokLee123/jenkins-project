package com.playdata.evalservice.eval.service;

import com.playdata.evalservice.client.CourseServiceClient;
import com.playdata.evalservice.client.OrderServiceClient;
import com.playdata.evalservice.client.UserServiceClient;
import com.playdata.evalservice.common.auth.TokenUserInfo;
import com.playdata.evalservice.common.dto.CommonResDto;
import com.playdata.evalservice.eval.dto.*;
import com.playdata.evalservice.eval.entity.Eval;
import com.playdata.evalservice.eval.repository.EvalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EvalService {

    private final EvalRepository evalRepository;
    private final UserServiceClient userServiceClient;
    private final CourseServiceClient courseServiceClient;
    private final OrderServiceClient orderServiceClient;


    public boolean canEvalCourse(TokenUserInfo userInfo, Long evalId) {

        Long userId = getUserId(userInfo);

        CommonResDto<List<Long>> resDto = orderServiceClient.userBuyIt(userId);
        List<Long> orderedProd = resDto.getResult();
        boolean containsed = orderedProd.contains(evalId);

        if(!containsed) {
            return false;
        }
        return true;
    }

    public EvalResDto createEval(TokenUserInfo userInfo, EvalSaveReqDto reqDto) {

        Long userId = getUserId(userInfo);

        Optional<Eval> foundEval
                = evalRepository.findByProductIdAndUserId(reqDto.getProductId(), userId);

        // 이미 해당 유저가 평가를 진행했다면
        if(foundEval.isPresent()) {
            return null;
        }

        Eval newEval = reqDto.toEntity(userId);
        Eval saved = evalRepository.save(newEval);

        return saved.fromEntity();
    }



    public boolean deleteEval(TokenUserInfo userInfo, Long evalId) {

        Long userId = getUserId(userInfo);

        Eval foundEval = evalRepository.findById(evalId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("없는 평가입니다.");
                });

        // 질문 작성자와 삭제하려는 유저가 다른 경우
        if(!foundEval.getUserId().equals(userId)) {
            return false;
        }

        evalRepository.delete(foundEval);
        return true;
    }


    public EvalResDto modifyEval(TokenUserInfo userInfo, EvalModiReqDto modiDto) {

        Long userId = getUserId(userInfo);

        Eval foundEval = evalRepository.findById(modiDto.getEvalId()).orElseThrow(() -> {
            return new EntityNotFoundException("없는 평가입니다.");
        });

        // 수정을 요청한 유저가 평가를 작성한 유저와 다른 경우
        if(!foundEval.getUserId().equals(userId)) {
            return null;
        }
        foundEval.setContent(modiDto.getContent());
        foundEval.setRating(modiDto.getRating());

        return evalRepository.save(foundEval).fromEntity();

    }


    public List<EvalResDto> findMyEval(TokenUserInfo userInfo) {

        Long userId = getUserId(userInfo);

        List<Eval> myEvals = evalRepository.findByUserId(userId).orElseThrow(() -> {
            return new EntityNotFoundException("해당 유저가 작성한 평가가 없습니다.");
        });

        return myEvals.stream().map(Eval::fromEntity).toList();
    }


    public EvalResDto findProdMyEval(TokenUserInfo userInfo, Long prodId) {

        Long userId = getUserId(userInfo);

        Eval myEval = evalRepository.findByProductIdAndUserId(prodId, userId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("해당 강의에 작성한 평가가 없습니다");
                });

        return myEval.fromEntity();

    }

    public List<EvalResDto> findProdAllEval(Long prodId) {

        List<Eval> evalList = evalRepository.findByProductId(prodId).orElseThrow(() -> {
            return new EntityNotFoundException("해당 강의의 평가가 없습니다");
        });

        List<EvalResDto> list = evalList.stream().map(Eval::fromEntity).toList();

        return list;
    }

    public Map<Long, Double> findCourseRating(List<Long> prodIdList) {

        List<ProductRatingAvgDto> ratingList = evalRepository.findAverageRatingGroupedByProduct();

        // prodIdList에 포함된 productId만 필터링
        List<ProductRatingAvgDto> filteredList = ratingList.stream()
                .filter(dto -> prodIdList.contains(dto.getProductId()))
                .toList();

        // 먼저 기본값 0.0으로 모든 ID 초기화
        Map<Long, Double> map = new HashMap<>();
        for (Long productId : prodIdList) {
            map.put(productId, 0.0);
        }

        // 실제 값이 있는 경우 덮어쓰기
        for (ProductRatingAvgDto dto : filteredList) {
            map.put(dto.getProductId(), dto.getAverageRating());
        }
        return map;
    }

    public EvalRateLenDto updateEvalInfo(Long prodId) {

        EvalRateLenDto informs
                = evalRepository.findRatingAndEvalCountByProductId(prodId);

        if(informs == null) {
            return null;
        }

        return informs;
    }

    public double findOneCourseRating(Long productId) {

        List<Eval> evalList = evalRepository.findByProductId(productId).orElseThrow(() -> {
            throw new EntityNotFoundException("해당 강의의 평가가 존재하지 않습니다!");
        });

        double avg = 0.0;
        for (Eval eval : evalList) {
            avg += eval.getRating();
        }

        return avg / evalList.size();

    }

    private Long getUserId(TokenUserInfo userInfo) {
        String email = userInfo.getEmail();

        CommonResDto<UserResDto> foundUser = userServiceClient.getIdByEmail(email);
        UserResDto result = foundUser.getResult();
        Long id = result.getId();

        log.info(result.toString());
        return id;

    }
}
