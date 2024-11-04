package com.example.moum.utils;


import java.util.HashMap;

public class ValueMap {

    public static final HashMap<String, Validation> codeToVal = new HashMap<String, Validation>() {
        {

            /*이메일 인증*/
            put("S-E001", Validation.EMAIL_AUTH_SUCCESS);
            put("F-E001", Validation.EMAIL_AUTH_FAILED);
            put("F-C002", Validation.EMAIL_NOT_FORMAL);
            put("F-C006", Validation.EMAIL_CODE_NOT_FORMAL);

            /*로그인*/
            put("S-M002", Validation.LOGIN_SUCCESS);
            put("F-A001", Validation.LOGIN_FAILED);

            /*채팅*/
            put("S-CH001", Validation.CHAT_POST_SUCCESS);
            put("F-CH001", Validation.CHAT_POST_FAIL);
            put("F-CH002", Validation.CHAT_RECEIVE_FAIL);
        }
    };

    public static Validation getCodeToVal(String code){
        return codeToVal.get(code);
    }
}