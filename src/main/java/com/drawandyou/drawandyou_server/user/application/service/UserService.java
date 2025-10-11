package com.drawandyou.drawandyou_server.user.application.service;

import com.drawandyou.drawandyou_server.user.domain.entity.User;
import com.drawandyou.drawandyou_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 새 사용자 등록
    public User create(final User userEntity) {
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        String username = userEntity.getUsername();

        // 같은 사용자명 존재 확인
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(userEntity);
    }

    // username, password 비교하여 사용자 반환
    public User getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
        User originalUser = userRepository.findByUsername(username);

        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        } // 입력값과 암호화된 값을 비교해줌

        return null;
    }
}
