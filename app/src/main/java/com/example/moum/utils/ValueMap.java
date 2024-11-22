package com.example.moum.utils;

import java.util.HashMap;

public class ValueMap {

    public static final HashMap<String, Validation> codeToVal = new HashMap<String, Validation>() {
        {
            // 이메일 인증
            put("S-E001", Validation.EMAIL_AUTH_SUCCESS);
            put("S-E002", Validation.EMAIL_AUTH_SUCCESS);
            put("F-E001", Validation.EMAIL_AUTH_FAILED);
            put("F-E002", Validation.EMAIL_AUTH_ALREADY);

            // 회원가입
            put("S-M001", Validation.SIGNUP_SUCCESS);

            // 로그인 및 로그아웃
            put("S-M002", Validation.LOGIN_SUCCESS);
            put("S-M004", Validation.LOGOUT_SUCCESS);
            put("F-A001", Validation.LOGIN_FAILED);
            put("F-M006", Validation.LOGOUT_ALREADY);

            // 채팅
            put("S-CH001", Validation.CHAT_POST_SUCCESS);
            put("F-CH001", Validation.CHAT_POST_FAIL);
            put("S-CH002", Validation.CHATROOM_MEMBER_LOAD_SUCCESS);
            put("F-CH002", Validation.CHAT_RECEIVE_FAIL);
            put("S-CH003", Validation.CHATROOM_CREATE_SUCCESS);
            put("F-CH003", Validation.CHATROOM_CREATE_FAIL);
            put("S-CH004", Validation.CHATROOM_UPDATE_SUCCESS);
            put("F-CH004", Validation.CHATROOM_UPDATE_FAIL);
            put("S-CH005", Validation.CHATROOM_INVITE_SUCCESS);
            put("F-CH005", Validation.CHATROOM_INVITE_FAIL);
            put("S-CH006", Validation.CHATROOM_DELETE_SUCCESS);
            put("F-CH006", Validation.CHATROOM_DELETE_FAIL);
            put("S-CH007", Validation.CHATROOM_GET_SUCCESS);
            put("F-CH007", Validation.CHATROOM_GET_FAIL);


            // 멤버 관련 성공 응답
            put("S-M003", Validation.REISSUE_SUCCESS);
            put("S-M005", Validation.GET_MY_INFO_SUCCESS);

            // 팀 관련 성공 응답
            put("S-T001", Validation.CREATE_TEAM_SUCCESS);
            put("S-T002", Validation.UPDATE_TEAM_SUCCESS);
            put("S-T003", Validation.DELETE_TEAM_SUCCESS);
            put("S-T005", Validation.GET_TEAM_SUCCESS);
            put("S-T006", Validation.GET_TEAM_LIST_SUCCESS);
            put("S-T007", Validation.GET_MY_TEAM_LIST_SUCCESS);
            put("S-T008", Validation.INVITE_MEMBER_SUCCESS);
            put("S-T009", Validation.KICK_MEMBER_SUCCESS);
            put("S-T010", Validation.LEAVE_TEAM_SUCCESS);

            // 게시글 관련 성공 응답
            put("S-A001", Validation.ARTICLE_LIST_GET_SUCCESS);
            put("S-A002", Validation.ARTICLE_GET_SUCCESS);
            put("S-A003", Validation.ARTICLE_POST_SUCCESS);
            put("S-A004", Validation.ARTICLE_UPDATE_SUCCESS);
            put("S-A005", Validation.ARTICLE_DELETE_SUCCESS);

            // JWT 관련 성공 응답
            put("S-J001", Validation.ACCESS_TOKEN_ISSUE_SUCCESS);

            // 댓글 관련 성공 응답
            put("S-C001", Validation.COMMENT_CREATE_SUCCESS);
            put("S-C002", Validation.COMMENT_UPDATE_SUCCESS);
            put("S-C003", Validation.COMMENT_DELETE_SUCCESS);

            // 좋아요 관련 성공 응답
            put("S-L001", Validation.LIKES_CREATE_SUCCESS);
            put("S-L002", Validation.LIKES_DELETE_SUCCESS);

            // 이력 관련 성공 응답
            put("S-R001", Validation.RECORD_ADD_SUCCESS);
            put("S-R002", Validation.RECORD_DELETE_SUCCESS);
            put("S-R003", Validation.RECORD_GET_SUCCESS);
            put("S-R004", Validation.RECORD_LIST_GET_SUCCESS);

            // 프로필 관련 성공 응답
            put("S-P001", Validation.GET_PROFILE_SUCCESS);
            put("S-P002", Validation.UPDATE_PROFILE_SUCCESS);

            // 공연 게시글 성공 응답
            put("S-PF001", Validation.PERFORMANCE_CREATE_SUCCESS);
            put("S-PF002", Validation.PERFORMANCE_UPDATE_SUCCESS);
            put("S-PF003", Validation.PERFORMANCE_DELETE_SUCCESS);
            put("S-PF004", Validation.PERFORMANCE_GET_SUCCESS);
            put("S-PF005", Validation.PERFORMANCE_LIST_GET_SUCCESS);
            put("S-PF006", Validation.PERFORMANCE_HOT_LIST_GET_SUCCESS);

            // 모음 관련 성공 응답
            put("S-MM001", Validation.CREATE_MOUM_SUCCESS);
            put("S-MM002", Validation.GET_MOUM_SUCCESS);
            put("S-MM003", Validation.UPDATE_MOUM_SUCCESS);
            put("S-MM004", Validation.DELETE_MOUM_SUCCESS);
            put("S-MM005", Validation.FINISH_MOUM_SUCCESS);
            put("S-MM006", Validation.REOPEN_MOUM_SUCCESS);
            put("S-MM007", Validation.UPDATE_PROCESS_MOUM_SUCCESS);

            // 공통 오류 코드
            put("F-C001", Validation.INTERNAL_SERVER_ERROR);
            put("F-C002", Validation.INVALID_INPUT_VALUE);
            put("F-C003", Validation.METHOD_NOT_ALLOWED);
            put("F-C004", Validation.INVALID_TYPE_VALUE);
            put("F-C005", Validation.BAD_CREDENTIALS);
            put("F-C006", Validation.ILLEGAL_ARGUMENT);

            // 멤버 관련 오류 코드
            put("F-M001", Validation.MEMBER_NOT_EXIST);
            put("F-M002", Validation.USER_NAME_ALREADY_EXISTS);
            put("F-M003", Validation.NO_AUTHORITY);
            put("F-M004", Validation.NEED_LOGIN);
            put("F-M005", Validation.AUTHENTICATION_NOT_FOUND);

            // 인증 관련 오류 코드
            put("F-A002", Validation.REFRESH_TOKEN_INVALID);
            put("F-A003", Validation.JWT_TOKEN_EXPIRED);

            // 게시글 관련 오류 코드
            put("F-AT001", Validation.ARTICLE_NOT_FOUND);
            put("F-AT002", Validation.ARTICLE_ALREADY_DELETED);

            // 위시리스트 관련 오류 코드
            put("F-W001", Validation.ALREADY_IN_WISHLIST);
            put("F-W002", Validation.ALREADY_DELETED_WISHLIST);

            // 댓글 관련 오류 코드
            put("F-CM001", Validation.COMMENT_NOT_FOUND);
            put("F-CM002", Validation.COMMENT_ALREADY_DELETED);

            // 좋아요 관련 오류 코드
            put("F-L001", Validation.DUPLICATE_LIKES);
            put("F-L002", Validation.LIKES_NOT_FOUND);
            put("F-L003", Validation.CANNOT_CREATE_SELF_LIKES);
            put("F-L004", Validation.CANNOT_DELETE_OTHERS_LIKES);

            // 팀 관련 오류 코드
            put("F-T001", Validation.MEMBER_ALREADY_INVITED);
            put("F-T002", Validation.TEAM_NOT_FOUND);
            put("F-T003", Validation.NOT_TEAM_MEMBER);
            put("F-T004", Validation.LEADER_CANNOT_LEAVE);
            put("F-T005", Validation.MUST_JOIN_TEAM_FIRST);

            // 신고 관련 성공 & 오류 코드
            put("F-RP001", Validation.REPORT_NOT_FOUND);
            put("S-RP002", Validation.REPORT_MEMBER_SUCCESS);
            put("F-RP002", Validation.REPORT_MEMBER_FAIL);
            put("F-RP012", Validation.REPORT_MEMBER_ALREADY);
            put("S-RP022", Validation.REPORT_MEMBER_GET_SUCCESS);
            put("S-RP003", Validation.REPORT_TEAM_SUCCESS);
            put("F-RP003", Validation.REPORT_TEAM_FAIL);
            put("F-RP013", Validation.REPORT_TEAM_ALREADY);
            put("S-RP023", Validation.REPORT_TEAM_GET_SUCCESS);
            put("S-RP004", Validation.REPORT_ARTICLE_SUCCESS);
            put("F-RP004", Validation.REPORT_ARTICLE_FAIL);
            put("F-RP014", Validation.REPORT_ARTICLE_ALREADY);
            put("S-RP024", Validation.REPORT_ARTICLE_GET_SUCCESS);
        }
    };

    public static Validation getCodeToVal(String code) {
        return codeToVal.get(code);
    }
}
