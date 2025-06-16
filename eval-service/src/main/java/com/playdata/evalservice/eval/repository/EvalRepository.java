package com.playdata.evalservice.eval.repository;

import com.playdata.evalservice.eval.dto.EvalRateLenDto;
import com.playdata.evalservice.eval.dto.ProductRatingAvgDto;
import com.playdata.evalservice.eval.entity.Eval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface EvalRepository extends JpaRepository<Eval, Long> {

    Optional<Eval> findByProductIdAndUserId(Long productId, Long userId);

    Optional<List<Eval>> findByUserId(Long userId);

    Optional<List<Eval>> findByProductId(Long productId);

    // productId별 평균 rating
    @Query("SELECT new com.playdata.evalservice.eval.dto.ProductRatingAvgDto(e.productId, AVG(e.rating)) " +
            "FROM Eval e GROUP BY e.productId")
    List<ProductRatingAvgDto> findAverageRatingGroupedByProduct();


    @Query("SELECT new com.playdata.evalservice.eval.dto.EvalRateLenDto(" +
            "e.productId, COUNT(e), AVG(e.rating)) " +
            "FROM Eval e WHERE e.productId = :productId GROUP BY e.productId")
    EvalRateLenDto findRatingAndEvalCountByProductId(@Param("productId") Long productId);

}
