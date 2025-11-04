package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Hobby;

import java.util.List;

public record HobbiesChangeRequest(
        List<Hobby> hobbies
) {
}
