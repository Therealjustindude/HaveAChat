package com.jdavies.haveachat_java_backend.module.check_in.repository;

import com.jdavies.haveachat_java_backend.module.check_in.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    @Query("SELECT c FROM CheckIn c WHERE c.golfCourseId = :courseId AND (c.expiresAt IS NULL AND c.checkInTime > :cutoff OR c.expiresAt > :now)")
    List<CheckIn> findActiveByCourse(@Param("courseId") Long courseId, @Param("cutoff") Instant cutoff, @Param("now") Instant now);
}
