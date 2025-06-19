package com.jdavies.haveachat_java_backend.module.golf_course.repository;

import com.jdavies.haveachat_java_backend.module.golf_course.model.GolfCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {
    List<GolfCourse> findByLatitudeBetweenAndLongitudeBetween(Double latMin, Double latMax, Double lngMin, Double lngMax);
}