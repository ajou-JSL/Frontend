package com.example.moum.utils;

public enum Validation {

    //not written
    ID_NOT_WRITTEN,
    NAME_NOT_WRITTEN,
    PASSWORD_NOT_WRITTEN,
    PASSWORD_CHECK_NOT_WRITTEN,
    EMAIL_NOT_WRITTEN,
    EMAIL_CODE_NOT_WRITTEN,
    NICKNAME_NOT_WRITTEN,
    INSTRUMENT_NOT_WRITTEN,
    PROFICIENCY_NOT_WRITTEN,

    //not formal
    ID_NOT_FORMAL,
    NAME_NOT_FORMAL,
    PASSWORD_NOT_FORMAL,
    EMAIL_NOT_FORMAL,
    EMAIL_CODE_NOT_FORMAL,
    PASSWORD_NOT_EQUAL,

    //email auth check
    EMAIL_AUTH_NOT_TRIED,
    EMAIL_ALREADY_AUTH,
    EMAIL_CODE_FAILED,
    EMAIL_CODE_NOT_CORRECT,

    //personal agreement
    PERSONAL_NOT_AGREE,

    //basic signup check
    BASIC_SIGNUP_NOT_TRIED,

    //login
    LOGIN_FAILED,

    //chat send & receive
    CHAT_POST_SUCCESS,
    CHAT_POST_FAIL,
    CHAT_RECEIVE_SUCCESS,
    CHAT_RECEIVE_FAIL,

    //chatroom
    CHATROOM_NAME_EMPTY,
    PARTICIPATE_AT_LEAST_TWO,

    //error
    NOT_VALID_ANYWAY,
    NETWORK_FAILED,

    //group
    GROUP_NOT_SELECTED,

    //valid all
    VALID_ALL
}