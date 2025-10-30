package com.klef.sdp.repository;

import com.klef.sdp.model.HeartRateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HeartRateRepository extends JpaRepository<HeartRateData, Long> {
    
    List<HeartRateData> findByUserUserIdOrderByMeasuredAtDesc(Long userId);
    
    List<HeartRateData> findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(h.heartRate) FROM HeartRateData h WHERE h.user.userId = :userId")
    Double findAverageHeartRateByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MAX(h.heartRate) FROM HeartRateData h WHERE h.user.userId = :userId")
    Integer findMaxHeartRateByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MIN(h.heartRate) FROM HeartRateData h WHERE h.user.userId = :userId")
    Integer findMinHeartRateByUserId(@Param("userId") Long userId);
    
    // FIXED: Correct method names
    int countByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    boolean existsByUserUserId(Long userId);
    boolean existsByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    List<HeartRateData> findTop3ByUserUserIdOrderByMeasuredAtDesc(Long userId);
}