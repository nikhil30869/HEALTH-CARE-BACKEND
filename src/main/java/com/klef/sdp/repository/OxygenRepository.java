package com.klef.sdp.repository;

import com.klef.sdp.model.OxygenData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OxygenRepository extends JpaRepository<OxygenData, Long> {
    
    List<OxygenData> findByUserUserIdOrderByMeasuredAtDesc(Long userId);
    
    List<OxygenData> findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(o.oxygenLevel) FROM OxygenData o WHERE o.user.userId = :userId")
    Double findAverageOxygenByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MAX(o.oxygenLevel) FROM OxygenData o WHERE o.user.userId = :userId")
    Double findMaxOxygenByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MIN(o.oxygenLevel) FROM OxygenData o WHERE o.user.userId = :userId")
    Double findMinOxygenByUserId(@Param("userId") Long userId);
    
    // FIXED: Correct method names
    int countByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    boolean existsByUserUserId(Long userId);
    boolean existsByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    List<OxygenData> findTop3ByUserUserIdOrderByMeasuredAtDesc(Long userId);
}