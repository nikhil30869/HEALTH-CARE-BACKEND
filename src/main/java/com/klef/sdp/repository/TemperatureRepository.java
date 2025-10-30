package com.klef.sdp.repository;

import com.klef.sdp.model.TemperatureData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<TemperatureData, Long> {
    
    List<TemperatureData> findByUserUserIdOrderByMeasuredAtDesc(Long userId);
    
    List<TemperatureData> findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(t.temperatureValue) FROM TemperatureData t WHERE t.user.userId = :userId")
    Double findAverageTemperatureByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MAX(t.temperatureValue) FROM TemperatureData t WHERE t.user.userId = :userId")
    Double findMaxTemperatureByUserId(@Param("userId") Long userId);
    
    @Query("SELECT MIN(t.temperatureValue) FROM TemperatureData t WHERE t.user.userId = :userId")
    Double findMinTemperatureByUserId(@Param("userId") Long userId);
    
    // FIXED: Correct method names
    int countByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    boolean existsByUserUserId(Long userId);
    boolean existsByUserUserIdAndMeasuredAtAfter(Long userId, LocalDateTime date);
    List<TemperatureData> findTop3ByUserUserIdOrderByMeasuredAtDesc(Long userId);
}