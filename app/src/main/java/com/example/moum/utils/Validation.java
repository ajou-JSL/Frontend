package com.example.moum.utils;

public enum Validation {

    //not written
    ID_NOT_WRITTEN,
    PASSWORD_NOT_WRITTEN,
    PASSWORD_CHECK_NOT_WRITTEN,
    EMAIL_NOT_WRITTEN,
    EMAIL_CODE_NOT_WRITTEN,
    NICKNAME_NOT_WRITTEN,
    INSTRUMENT_NOT_WRITTEN,
    PROFICIENCY_NOT_WRITTEN,

    //not formal
    ID_NOT_FORMAL,
    PASSWORD_NOT_FORMAL,
    EMAIL_NOT_FORMAL,
    EMAIL_CODE_NOT_FORMAL,
    PASSWORD_NOT_EQUAL,

    //email auth check
    EMAIL_AUTH_SUCCESS,
    EMAIL_AUTH_FAILED,
    EMAIL_AUTH_NOT_TRIED,

    //personal agreement
    PERSONAL_NOT_AGREE,

    //basic signup check
    BASIC_SIGNUP_NOT_TRIED,

    //login
    LOGIN_SUCCESS,
    LOGIN_FAILED,

    //chat send & receive
    CHAT_POST_SUCCESS,
    CHAT_POST_FAIL,
    CHAT_RECEIVE_SUCCESS,
    CHAT_RECEIVE_FAIL,

    //error
    NOT_VALID_ANYWAY,
    NETWORK_FAILED,

    //valid all
    VALID_ALL
}