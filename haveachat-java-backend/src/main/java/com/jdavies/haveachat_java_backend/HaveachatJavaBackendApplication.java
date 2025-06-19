package com.jdavies.haveachat_java_backend;

import com.jdavies.haveachat_java_backend.module.channel.service.ChannelService;
import com.jdavies.haveachat_java_backend.module.golf_course.model.GolfCourse;
import com.jdavies.haveachat_java_backend.module.golf_course.repository.GolfCourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HaveachatJavaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaveachatJavaBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner seedCourses(GolfCourseRepository repo, ChannelService channelService) {
        return args -> {
            String buildStage = System.getenv("BUILD_STAGE");
            if (!"development".equalsIgnoreCase(buildStage)) {
                return;
            }

            if (repo.count() == 0) {
                GolfCourse course1 = new GolfCourse();
                course1.setName("Green Valley");
                course1.setLatitude(38.8951);
                course1.setLongitude(-77.0364);
                course1.setAddress("123 Main St");
                repo.save(course1);
                channelService.findOrCreateCourseChannel(course1.getName());

                GolfCourse course2 = new GolfCourse();
                course2.setName("Sunset Hills");
                course2.setLatitude(38.8977);
                course2.setLongitude(-77.0365);
                course2.setAddress("456 Golf Rd");
                repo.save(course2);
                channelService.findOrCreateCourseChannel(course2.getName());
            }
        };
    }

}