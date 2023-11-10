package com.hacker.hackathon.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage {
    /**
     * auth
     */
    LOGIN_SUCCESS(CREATED, "로그인이 성공했습니다."),
    SIGNUP_SUCCESS(CREATED, "회원가입에 성공했습니다."),
    TOKEN_RE_ISSUE_SUCCESS(CREATED, "토큰 재발급에 성공했습니다."),

    /**
     * user
     */
    GET_VIDEOS_BY_USER_SUCCESS(OK, "유저별 동영상을 불러오는데 성공했습니다."),

    UPDATE_USER_SUCCESS(OK, "유저를 업데이트하는데 성공했습니다."),

    DELETE_USER_SUCCESS(OK, "유저를 삭제하는데 성공했습니다."),

    GET_USER_SUCCESS(OK, "유저를 불러오는데 성공했습니다."),
    GET_USER_LIST_SUCCESS(OK, "유저 전체를 불러오는데 성공했습니다,"),

    /**
     * video
     */
    CREATE_VIDEO_SUCCESS(CREATED, "비디오 생성에 성공했습니다."),
    GET_POPULAR_VIDEO_SUCCESS(OK, "인기 동영상 조회에 성공했습니다."),

    GET_CATEGORY_VIDEO_SUCCESS(OK, "카테고리별 동영상 조회에 성공했습니다."),

    VIEW_VIDEO_SUCCESS(OK, "동영상 보기에 성공했습니다."),

    FIND_VIDEO_SUCCESS(OK,"동영상 조회에 성공했습니다."),

    VIDEO_VIEW_DONE(OK, "동영상 보기를 완료했습니다."),

    /**
     * transcript
     */
    GET_TRANSCRIPT_SUCCESS(OK, "자막을 불러오는데 성공했습니다."),

    EVALUATION_SUCCESS(OK, "발음을 분석하는데 성공했습니다."),
    GET_PREVIOUS_EVALUATION(OK, "기존 분석을 가져오는데 성공했습니다."),

    GET_TRANSCRIPT_AUDIO_SUCCESS(OK, "자막 음성을 불러오는데 성공했습니다."),


    /**
     * evaluations
     */

    GET_ALL_EVALUATIONS(OK, "전체 분석을 불러오는데 성공했습니다."),


    /**
     * openAI
     */

    OPENAI_SUCCESS(OK, "OpenAI response 가져오기 성공"),

    /**
     * leaderboard
     */
    LEADERBOARD_SUCCESS(OK, "리더보드 가져오기 성공."),
    GET_TRANSLATIONS_SUCCESS(OK, "translation 가져오기 성공" ),


    /**
     * quiz
     */
    GET_QUIZ_SUCCESS(OK,"퀴즈 가져오기 성공"),
    QUIZ_SOLVE_SUCCESS(OK,"퀴즈 풀이를 성공했습니다, 맞았는지 틀린지는 메시지 보셈"),
    /**
     * todolist
     */
    GET_VIDEOS_BY_STAGE_SUCCESS(OK,"스테이지별 동영상 가져오기 성공"),
    USER_TODO_LIST_GET_SUCCESS(OK, "유저별 투두리스트 가져오기 성공"),
    TODO_LIST_GET_SUCCESS(OK,"투두리스트 전체 반환 성공."),

    TODO_LIST_GET_BY_ID_SUCCESS(OK,"투두리스트 개별 반환 성공."),

    USER_TODO_LIST_UPDATE_SUCCESS(OK, "유저별 투두리스트 업데이트 성공"),
    MAKE_NEW_USER_TODO_LIST_SUCCESS(OK,"유저 투두리스트 생성 성공"),
    TODO_LIST_DELETE_SUCCESS(OK,"유저별 투두리스트 삭제 성공");
    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
