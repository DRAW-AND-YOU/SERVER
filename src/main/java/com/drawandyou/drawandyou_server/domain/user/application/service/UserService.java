package com.drawandyou.drawandyou_server.domain.user.application.service;

import com.drawandyou.drawandyou_server.domain.article.application.service.ArticleService;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service.DrawingAnalyzeService;
import com.drawandyou.drawandyou_server.domain.therapyprogram.application.service.TherapyProgramService;
import com.drawandyou.drawandyou_server.domain.user.exception.*;
import com.drawandyou.drawandyou_server.domain.user.presentation.dto.request.*;
import com.drawandyou.drawandyou_server.domain.user.presentation.dto.response.*;
import com.drawandyou.drawandyou_server.global.auth.presentation.dto.UserAuthDto;
import com.drawandyou.drawandyou_server.global.security.TokenProvider;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import com.drawandyou.drawandyou_server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserFindService userFindService;
    private final TherapyProgramService therapyProgramService;
    private final ArticleService articleService;
    private final DrawingAnalyzeService drawingAnalyzeService;

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequestDto) {

        if (registerRequestDto == null || registerRequestDto.password() == null) {
            throw new InvalidPasswordException();
        }

        User user = User.builder()
                .email(registerRequestDto.email())
                .password(passwordEncoder.encode(registerRequestDto.password()))
                .username(registerRequestDto.username())
                .nickname(registerRequestDto.nickname())
                .birthDate(registerRequestDto.birthDate())
                .gender(registerRequestDto.gender())
                .hobbies(registerRequestDto.hobbies())
                .build();

        User registeredUser = create(user);

        String accessToken = tokenProvider.create(registeredUser);

        return RegisterResponse.builder()
                .userId(registeredUser.getId())
                .username(registeredUser.getUsername())
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public User create(final User userEntity) {
        String getEmail = userEntity.getEmail();

        // 같은 이메일 존재 확인
        if (userRepository.existsByEmail(getEmail)) {
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(userEntity);
    }

    // email, password 비교하여 사용자 반환
    public User getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        User originalUser = userRepository.findOptionalByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!encoder.matches(password, originalUser.getPassword())) {
            throw new InvalidPasswordException();
        }

        return originalUser;
    }


    public LoginResponse signIn(String email, String password) {

        User user = getByCredentials(email, password, passwordEncoder);
        // 인증 성공시 jwt 토큰 발급
        final String token = tokenProvider.create(user);

        // 응답 객체에 사용자 정보 및 토큰 포함 (비밀번호 같은 민감 정보 포함 x)
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(token)
                .build();
    }

    /**
     * 사용자 ID로 사용자 정보 조회
     * @param userId 사용자 ID
     * @return UserAuthDto (비밀번호 제외)
     */
    public CurrentLoginUserResponse getUserById(Long userId) {
        User user = userFindService.findUser(userId);

        boolean isSocialUser = user.getAuthProvider() != null;
        boolean isRegisterCompleted = checkRegisterCompleteStatusOfUser(user);


        return CurrentLoginUserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .isSocialUser(isSocialUser)
                .isRegisterCompleted(isRegisterCompleted)
                .build();
    }

    private boolean checkRegisterCompleteStatusOfUser(User user) {
        // 이름, 닉네임, 생년월일, 성별 정보가 입력되어 있으면  Complete 한 것으로 판단
        return user.getUsername() != null && user.getNickname() != null
                && user.getBirthDate() != null && user.getGender() != null;
    }

    /**
     * 사용자 ID로 JWT 토큰 발급
     * @param userId 사용자 ID
     * @return UserAuthDto (사용자 정보 + JWT 토큰)
     */
    public UserAuthDto issueTokenByUserId(Long userId) {
        User user = userFindService.findUser(userId);

        // 사용자 ID 기반으로 JWT 토큰 생성
        final String token = tokenProvider.createByUserId(userId);

        return UserAuthDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .build();
    }

    public CheckUserNameResponse checkUsernameAvailable(String email) {
        Boolean isExists = userRepository.existsByEmail(email);
        return new CheckUserNameResponse(!isExists);
    }

    public UserMyPageResponse getUserMyPage(Long userId) {
        User user = userFindService.findUser(userId);
        Hibernate.initialize(user.getHobbies());
        return UserMyPageResponse.toMyPageResponse(user);
    }


    @Transactional
    public RegisterResponse processExtraSignUpForSocialLoginUser(Long userId, ExtraRegisterRequest extraRegisterRequest) {
        User user = userFindService.findUser(userId);

        // 소셜 로그인 유저가 아니라면 예외를 던진다.
        if (user.getAuthProvider() == null || user.getAuthProvider().isEmpty()) {
            throw new NotSocialLoginUserException();
        }
        // nickname, birthdate, gender, hobbies
        user.assignNickname(extraRegisterRequest.nickname());
        user.assignBirthDate(extraRegisterRequest.birthDate());
        user.assignGender(extraRegisterRequest.gender());
        user.assignHobbies(extraRegisterRequest.hobbies());

        String accessToken = tokenProvider.create(user);

        return RegisterResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public UserMyPageResponse changeProfileImageUrlForUser(Long userId, ProfileImageChangeRequest profileImageChangeRequest) {
        User user = userFindService.findUser(userId);
        user.changeProfileImage(profileImageChangeRequest.profileImageUrl());

        Hibernate.initialize(user.getHobbies());
        return UserMyPageResponse.toMyPageResponse(user);
    }

    @Transactional
    public UserMyPageResponse changePasswordForUser(Long userId, PasswordChangeRequest passwordChangeRequest) {
        User user = userFindService.findUser(userId);

        boolean passwordMatches = passwordEncoder.matches(passwordChangeRequest.currentPassword(), user.getPassword());
        // request 로 전달받은 비밀번호와 db 의 비밀번호가 일치하지 않으면 예외 발생
        if (!passwordMatches) throw new CanNotModifyPasswordException();

        String newPassword = passwordEncoder.encode(passwordChangeRequest.newPassword());
        user.changePassword(newPassword);

        Hibernate.initialize(user.getHobbies());
        return UserMyPageResponse.toMyPageResponse(user);
    }

    @Transactional
    public UserMyPageResponse changeHobbiesForUser(Long userId, HobbiesChangeRequest hobbiesChangeRequest) {
        User user = userFindService.findUser(userId);
        user.assignHobbies(hobbiesChangeRequest.hobbies());
        Hibernate.initialize(user.getHobbies());
        return UserMyPageResponse.toMyPageResponse(user);
    }

    public DashBoardStatsResponse getDashboardStats(Long userId) {
        Long drawingCount = drawingAnalyzeService.getDrawingAnalysisCount(userId);
        Long articleCount = articleService.getArticleCount(userId);
        Long completedProgramCount = therapyProgramService.getCompletedProgramCount(userId);

        return DashBoardStatsResponse.of(drawingCount, articleCount, completedProgramCount);
    }
}
