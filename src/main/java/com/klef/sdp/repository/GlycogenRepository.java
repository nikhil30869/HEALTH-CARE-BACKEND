package com.klef.sdp.repository;

import com.klef.sdp.model.GlycogenData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GlycogenRepository extends JpaRepository<GlycogenData, Long> {
    
    List<GlycogenData> findByUserUserIdOrderByMeasuredAtDesc(Long userId);
    
    List<GlycogenData> findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(g.glycogenLevel) FROM GlycogenData g WHERE g.user.userId = :userId")
    Double findAverageGlycogenByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MAX(g.glycogenLevel) FROM GlycogenData g WHERE g.user.userId = :userId")
    Double findMaxGlycogenByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MIN(g.glycogenLevel) FROM GlycogenData g WHERE g.user.userId = :userId")
    Double findMinGlycogenByUserId(@Param("userId") Long userId);
    
    // FIXED: Correct method names
    int countByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    boolean existsByUserUserId(Long userId);
    List<GlycogenData> findTop3ByUserUserIdOrderByMeasuredAtDesc(Long userId);
}