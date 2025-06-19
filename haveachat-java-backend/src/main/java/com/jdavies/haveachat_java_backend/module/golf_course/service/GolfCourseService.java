package com.jdavies.haveachat_java_backend.module.golf_course.service;

import com.jdavies.haveachat_java_backend.module.golf_course.model.GolfCourse;
import com.jdavies.haveachat_java_backend.module.golf_course.repository.GolfCourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GolfCourseService {
    private final GolfCourseRepository repo;

    public GolfCourseService(GolfCourseRepository repo) {
        this.repo = repo;
    }

    public GolfCourse getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<GolfCourse> findNearby(double lat, double lng, double radiusDegrees) {
        return repo.findByLatitudeBetweenAndLongitudeBetween(
                lat - radiusDegrees, lat + radiusDegrees,
                lng - radiusDegrees, lng + radiusDegrees
        );
    }

    public GolfCourse create(GolfCourse course) {
        return repo.save(course);
    }
}
