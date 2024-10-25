package com.example.moum.utils;

public enum Validation {

    //not written
    NAME_NOT_WRITTEN,
    PASSWORD_NOT_WRITTEN,
    PASSWORD_CHECK_NOT_WRITTEN,
    EMAIL_NOT_WRITTEN,
    EMAIL_CODE_NOT_WRITTEN,
    NICKNAME_NOT_WRITTEN,
    INSTRUMENT_NOT_WRITTEN,
    PROFICIENCY_NOT_WRITTEN,

    //not formal
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

    //error
    NOT_VALID_ANYWAY,
    NETWORK_FAILED,

    //valid all
    VALID_ALL
}