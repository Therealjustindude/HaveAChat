package com.jdavies.haveachat_java_backend.module.golf_course.controller;

import com.jdavies.haveachat_java_backend.module.golf_course.model.GolfCourse;
import com.jdavies.haveachat_java_backend.module.golf_course.service.GolfCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class GolfCourseController {

    private final GolfCourseService courseService;

    public GolfCourseController(GolfCourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GolfCourse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<GolfCourse>> getNearbyCourses(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "0.2") double radius
    ) {
        return ResponseEntity.ok(courseService.findNearby(lat, lng, radius));
    }

    @PostMapping("")
    public ResponseEntity<GolfCourse> create(@RequestBody GolfCourse course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(course));
    }
}