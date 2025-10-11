package com.drawandyou.drawandyou_server.user.application.service;

import com.drawandyou.drawandyou_server.global.auth.presentation.dto.UserAuthDto;
import com.drawandyou.drawandyou_server.global.security.TokenProvider;
import com.drawandyou.drawandyou_server.user.domain.entity.User;
import com.drawandyou.drawandyou_server.user.domain.repository.UserRepository;
import com.drawandyou.drawandyou_server.user.exception.InvalidPasswordException;
import com.drawandyou.drawandyou_server.user.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserAuthDto registerUser(UserAuthDto userDTO) {

        if (userDTO == null || userDTO.password() == null) {
            throw new InvalidPasswordException();
        }

        User user = User.builder()
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .build();

        User registeredUser = create(user);

        return UserAuthDto.builder()
                .id(registeredUser.getId())
                .username(registeredUser.getUsername())
                .build();
    }

    public User create(final User userEntity) {
        String username = userEntity.getUsername();

        // 같은 사용자명 존재 확인
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException();
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


    public UserAuthDto signIn(String username, String password) {

        User user = getByCredentials(username, password, passwordEncoder);
        // 인증 성공시 jwt 토큰 발급
        final String token = tokenProvider.create(user);

        // 응답 객체에 사용자 정보 및 토큰 포함 (비밀번호 같은 민감 정보 포함 x)

        return UserAuthDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .token(token)
                .build();
    }
}
