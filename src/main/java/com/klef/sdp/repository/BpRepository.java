package com.klef.sdp.repository;

import com.klef.sdp.model.BpData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BpRepository extends JpaRepository<BpData, Long> {
    
    List<BpData> findByUserUserIdOrderByMeasuredAtDesc(Long userId);
    
    List<BpData> findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(b.systolic) FROM BpData b WHERE b.user.userId = :userId")
    Double findAverageSystolicByUserId(@Param("userId") Long userId);
    
    @Query("SELECT AVG(b.diastolic) FROM BpData b WHERE b.user.userId = :userId")
    Double findAverageDiastolicByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MAX(b.systolic) FROM BpData b WHERE b.user.userId = :userId")
    Integer findMaxSystolicByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MIN(b.systolic) FROM BpData b WHERE b.user.userId = :userId")
    Integer findMinSystolicByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MAX(b.diastolic) FROM BpData b WHERE b.user.userId = :userId")
    Integer findMaxDiastolicByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MIN(b.diastolic) FROM BpData b WHERE b.user.userId = :userId")
    Integer findMinDiastolicByUserId(@Param("userId") Long userId);
    
    // FIXED: Correct method names
    int countByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    boolean existsByUserUserId(Long userId);
    List<BpData> findTop3ByUserUserIdOrderByMeasuredAtDesc(Long userId);
}