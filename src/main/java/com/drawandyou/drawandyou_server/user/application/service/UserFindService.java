package com.drawandyou.drawandyou_server.user.application.service;

import com.drawandyou.drawandyou_server.user.domain.entity.User;
import com.drawandyou.drawandyou_server.user.domain.repository.UserRepository;
import com.drawandyou.drawandyou_server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFindService {

    private final UserRepository userRepository;

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
