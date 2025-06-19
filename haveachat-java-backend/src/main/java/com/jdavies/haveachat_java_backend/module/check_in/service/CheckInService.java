package com.jdavies.haveachat_java_backend.module.check_in.service;

import com.jdavies.haveachat_java_backend.module.channel.model.Channel;
import com.jdavies.haveachat_java_backend.module.channel.model.ChannelMember;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelMemberService;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelService;
import com.jdavies.haveachat_java_backend.module.check_in.model.CheckIn;
import com.jdavies.haveachat_java_backend.module.check_in.repository.CheckInRepository;
import com.jdavies.haveachat_java_backend.module.golf_course.model.GolfCourse;
import com.jdavies.haveachat_java_backend.module.golf_course.service.GolfCourseService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CheckInService {

    private final CheckInRepository repo;
    private final ChannelService channelService;
    private final ChannelMemberService channelMemberService;

    private final GolfCourseService golfCourseService;

    public CheckInService(CheckInRepository repo, ChannelService channelService, ChannelMemberService channelMemberService, GolfCourseService golfCourseService) {
        this.repo = repo;
        this.channelService = channelService;
        this.channelMemberService = channelMemberService;
        this.golfCourseService = golfCourseService;
    }

    public CheckIn createCheckIn(CheckIn checkIn, Long userId) {
        checkIn.setUserId(userId);
        checkIn.setCheckInTime(Instant.now());
        CheckIn saved = repo.save(checkIn);

        GolfCourse course = golfCourseService.getById(checkIn.getGolfCourseId());

        // ⛳ Auto-join course channel
        Channel courseChannel = channelService.findOrCreateCourseChannel(course.getName());
        ChannelMember member = new ChannelMember(userId, courseChannel.getId());
        channelMemberService.addMember(member);

        return saved;
    }

    public List<CheckIn> getActiveForCourse(Long courseId) {
        Instant now = Instant.now();
        Instant cutoff = now.minus(Duration.ofHours(4));
        return repo.findActiveByCourse(courseId, cutoff, now);
    }
}