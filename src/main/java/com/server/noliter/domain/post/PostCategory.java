package com.server.noliter.domain.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PostCategory {
    ETC("기타", "ETC"),
    EDUCATION("교육", "EDUCATION"),
    SOFTWARE("소프트웨어", "S/W"),
    HARDWARE("하드웨어", "H/W"),
    GAME("게임", "GAME"),
    ART("예술", "ART"),
    CULTURE("문화", "CULTURE"),
    LIVING("생활", "LIVING"),
    HEALTH("건강", "HEALTH"),
    SOCIETY("사회", "SOCIETY"),
    ECONOMY("경제", "ECONOMY"),
    TRAVEL("여행", "TRAVEL"),
    SHOPPING("쇼핑", "SHOPPING"),
    SPORTS("운동", "SPORTS");

    private final String langKor;
    private final String langEng;

    PostCategory(String langKor, String langEng){
        this.langKor = langKor;
        this.langEng = langEng;
    }
}
