package com.example.moum.utils;

public enum Validation {

    /*FRONTEND 고유 에러 LIST*/

    //NOT WRITTEN
    ID_NOT_WRITTEN,
    PASSWORD_NOT_WRITTEN,
    PASSWORD_CHECK_NOT_WRITTEN,
    EMAIL_NOT_WRITTEN,
    EMAIL_CODE_NOT_WRITTEN,
    NICKNAME_NOT_WRITTEN,
    DESCRIPTION_NOT_WRITTEN,
    INSTRUMENT_NOT_WRITTEN,
    PROFICIENCY_NOT_WRITTEN,
    TEAM_NAME_NOT_WRITTEN,
    TEAM_GENRE_NOT_WRITTEN,
    MOUM_NAME_NOT_WRITTEN,
    GENRE_NOT_WRITTEN,
    SETTLEMENT_NAME_NOT_WRITTEN,
    SETTLEMENT_FEE_NOT_WRITTEN,
    RECORD_NAME_NOT_WRITTEN,
    MUSIC_NAME_NOT_WRITTEN,
    ARTIST_NAME_NOT_WRITTEN,

    //NOT FORMAL
    ID_NOT_FORMAL,
    PASSWORD_NOT_FORMAL,
    EMAIL_NOT_FORMAL,
    EMAIL_CODE_NOT_FORMAL,
    PASSWORD_NOT_EQUAL,
    ID_NOT_EQUAL,
    VIDEO_URL_NOT_FORMAL,
    DATE_NOT_VALID,

    //개인정보 동의
    PERSONAL_NOT_AGREE,

    //회원가입
    BASIC_SIGNUP_NOT_TRIED,
    RECORD_NOT_VALID,

    // 채팅방
    CHATROOM_NOT_LOADED,
    CHATROOM_NAME_EMPTY,
    PARTICIPATE_AT_LEAST_TWO,
    GROUP_NOT_SELECTED,
    CHATROOM_CREATE_SUCCESS,
    CHATROOM_CREATE_FAIL,
    CHATROOM_MEMBER_LOAD_SUCCESS,
    CHATROOM_ALREADY_EXIST,
    CHATROOM_WITH_ME,
    CHATROOM_UPDATE_SUCCESS,
    CHATROOM_UPDATE_FAIL,
    CHATROOM_INVITE_SUCCESS,
    CHATROOM_INVITE_FAIL,
    CHATROOM_DELETE_SUCCESS,
    CHATROOM_DELETE_FAIL,
    CHATROOM_GET_SUCCESS,
    CHATROOM_GET_FAIL,

    // 프로필
    PROFILE_NOT_LOADED,

    //이외 에러 처리
    NOT_VALID_ANYWAY,
    NETWORK_FAILED,
    VALID_ALL,

    // 신고
    REPORT_MEMBER_SUCCESS,
    REPORT_MEMBER_FAIL,
    REPORT_MEMBER_ALREADY,
    REPORT_MEMBER_GET_SUCCESS,
    REPORT_NOT_FOUND,
    REPORT_TEAM_SUCCESS,
    REPORT_TEAM_FAIL,
    REPORT_TEAM_ALREADY,
    REPORT_TEAM_GET_SUCCESS,
    REPORT_ARTICLE_SUCCESS,
    REPORT_ARTICLE_FAIL,
    REPORT_ARTICLE_ALREADY,
    REPORT_ARTICLE_GET_SUCCESS,

    /*BACKEND 에러 LIST*/

    // 이메일 인증
    EMAIL_AUTH_SUCCESS,
    EMAIL_AUTH_FAILED,
    EMAIL_AUTH_ALREADY,
    EMAIL_AUTH_NOT_TRIED,

    // 회원가입
    SIGNUP_SUCCESS,
    SIGNOUT_SUCCESS,

    // 로그인 및 로그아웃
    LOGIN_SUCCESS,
    LOGOUT_SUCCESS,
    LOGIN_FAILED,
    LOGOUT_ALREADY,
    SIGNOUT_MEMBER,

    // 채팅
    CHAT_POST_SUCCESS,
    CHAT_POST_FAIL,
    CHAT_RECEIVE_SUCCESS,
    CHAT_RECEIVE_FAIL,

    // 멤버 관련 성공 응답
    REISSUE_SUCCESS,
    GET_MY_INFO_SUCCESS,

    // 팀 관련 성공 응답
    CREATE_TEAM_SUCCESS,
    UPDATE_TEAM_SUCCESS,
    DELETE_TEAM_SUCCESS,
    GET_TEAM_SUCCESS,
    GET_TEAM_LIST_SUCCESS,
    GET_MY_TEAM_LIST_SUCCESS,
    INVITE_MEMBER_SUCCESS,
    KICK_MEMBER_SUCCESS,
    LEAVE_TEAM_SUCCESS,

    // 게시글 관련 성공 응답
    ARTICLE_LIST_GET_SUCCESS,
    ARTICLE_GET_SUCCESS,
    ARTICLE_POST_SUCCESS,
    ARTICLE_UPDATE_SUCCESS,
    ARTICLE_DELETE_SUCCESS,

    // JWT 관련 성공 응답
    ACCESS_TOKEN_ISSUE_SUCCESS,

    // 댓글 관련 성공 응답
    COMMENT_CREATE_SUCCESS,
    COMMENT_UPDATE_SUCCESS,
    COMMENT_DELETE_SUCCESS,

    // 좋아요 관련 성공 응답
    LIKES_GET_SUCCESS,
    LIKES_CREATE_SUCCESS,
    LIKES_DELETE_SUCCESS,

    // 이력 관련 성공 응답
    RECORD_ADD_SUCCESS,
    RECORD_DELETE_SUCCESS,
    RECORD_GET_SUCCESS,
    RECORD_LIST_GET_SUCCESS,

    // 프로필 관련 성공 응답
    GET_PROFILE_SUCCESS,
    UPDATE_PROFILE_SUCCESS,

    // 공연 게시글 관련 성공 응답
    PERFORMANCE_CREATE_SUCCESS,
    PERFORMANCE_UPDATE_SUCCESS,
    PERFORMANCE_DELETE_SUCCESS,
    PERFORMANCE_GET_SUCCESS,
    PERFORMANCE_LIST_GET_SUCCESS,
    PERFORMANCE_HOT_LIST_GET_SUCCESS,

    // 모음 관련 성공 응답
    CREATE_MOUM_SUCCESS,
    GET_MOUM_SUCCESS,
    UPDATE_MOUM_SUCCESS,
    DELETE_MOUM_SUCCESS,
    FINISH_MOUM_SUCCESS,
    REOPEN_MOUM_SUCCESS,
    UPDATE_PROCESS_MOUM_SUCCESS,

    // 연습실, 공연장 응답
    PRACTICE_ROOM_GET_SUCCESS,
    PERFORMANCE_HALL_GET_SUCCESS,
    PRACTICE_ROOM_NOT_FOUND,
    PERFORMANCE_HALL_NOT_FOUND,
    PARAMETER_NOT_VALID,

    // 모음 + 연습실, 공연장 응답
    MOUM_PRACTICE_ROOM_CREATE_SUCCESS,
    MOUM_PRACTICE_ROOM_DELETE_SUCCESS,
    MOUM_PRACTICE_ROOM_GET_SUCCESS,
    MOUM_PERFORMANCE_HALL_CREATE_SUCCESS,
    MOUM_PERFORMANCE_HALL_DELETE_SUCCESS,
    MOUM_PERFORMANCE_HALL_GET_SUCCESS,
    MOUM_PRACTICE_ROOM_NOT_FOUND,
    MOUM_PERFORMANCE_HALL_NOT_FOUND,

    // QR(웹 팜플렛) 관련 응답
    QR_SUCCESS,
    QR_FAIL,
    QR_PERFORM_NOT_FOUND,
    QR_DELETE_SUCCESS,

    // 공통 오류 코드
    INTERNAL_SERVER_ERROR,
    INVALID_INPUT_VALUE,
    METHOD_NOT_ALLOWED,
    INVALID_TYPE_VALUE,
    BAD_CREDENTIALS,
    ILLEGAL_ARGUMENT,

    // 멤버 관련 오류 코드
    MEMBER_NOT_EXIST,
    USER_NAME_ALREADY_EXISTS,
    NO_AUTHORITY,
    NEED_LOGIN,
    AUTHENTICATION_NOT_FOUND,
    MEMBER_ALREADY_LOGOUT,
    SIGNOUTED_MEMBER,
    BANNED_MEMBER,

    // 인증 관련 오류 코드
    LOGIN_FAIL,
    REFRESH_TOKEN_INVALID,
    JWT_TOKEN_EXPIRED,

    // 게시글 관련 오류 코드
    ARTICLE_NOT_FOUND,
    ARTICLE_GET_FAILED,
    ARTICLE_ALREADY_DELETED,

    // 위시리스트 관련 오류 코드
    ALREADY_IN_WISHLIST,
    ALREADY_DELETED_WISHLIST,

    // 댓글 관련 오류 코드
    COMMENT_NOT_FOUND,
    COMMENT_ALREADY_DELETED,

    // 좋아요 관련 오류 코드
    DUPLICATE_LIKES,
    LIKES_NOT_FOUND,
    CANNOT_CREATE_SELF_LIKES,
    CANNOT_DELETE_OTHERS_LIKES,

    // 팀 관련 오류 코드
    MEMBER_ALREADY_INVITED,
    TEAM_NOT_FOUND,
    NOT_TEAM_MEMBER,
    LEADER_CANNOT_LEAVE,
    MUST_JOIN_TEAM_FIRST,

    // 공연 게시글 관련 오류 코드
    PERFORMANCE_NOT_FOUNT,
    PERFORMANCE_ALREADY_MADE,

    // 정산 관련 성공 & 오류 코드
    SETTLEMENT_CREATE_SUCCESS,
    SETTLEMENT_GET_SUCCESS,
    SETTLEMENT_DELETE_SUCCESS,
    MOUM_NOT_FOUND,

}