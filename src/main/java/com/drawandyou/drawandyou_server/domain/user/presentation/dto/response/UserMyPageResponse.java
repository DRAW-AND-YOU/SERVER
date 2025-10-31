package com.drawandyou.drawandyou_server.domain.user.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import com.nimbusds.jose.crypto.PasswordBasedDecrypter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public record UserMyPageResponse(
        String profileImageUrl,
        String nickname,
        String username,
        String email,
        LocalDate birthDate,
        String gender,
        List<String> hobbies
) {

    public static UserMyPageResponse toMyPageResponse(User user){
        return new UserMyPageResponse(
                user.getProfileImageUrl(),
                user.getNickname(),
                user.getUsername(),
                user.getEmail(),
                user.getBirthDate(),
                user.getGender().getDisplayName(),
                user.getHobbies()
        );
    }

}
