package com.jdavies.haveachat_java_backend.module.check_in.controller;

import com.jdavies.haveachat_java_backend.common.annotation.CurrentUser;
import com.jdavies.haveachat_java_backend.module.check_in.dto.CheckInRequest;
import com.jdavies.haveachat_java_backend.module.check_in.model.CheckIn;
import com.jdavies.haveachat_java_backend.module.check_in.service.CheckInService;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkins")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping("")
    public ResponseEntity<CheckIn> create(
            @RequestBody CheckInRequest req,
            @CurrentUser User user
    ) {
        CheckIn checkIn = new CheckIn();
        checkIn.setGolfCourseId(req.getGolfCourseId());
        checkIn.setIntent(req.getIntent());
        checkIn.setMessage(req.getMessage());
        checkIn.setOpenToChat(req.isOpenToChat());
        checkIn.setExpiresAt(req.getExpiresAt());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(checkInService.createCheckIn(checkIn, user.getId()));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CheckIn>> getActiveForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(checkInService.getActiveForCourse(courseId));
    }
}
