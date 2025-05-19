package com.jdavies.haveachat_java_backend.repository;

import com.jdavies.haveachat_java_backend.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
        Optional<Channel> findByName(String name);
}
