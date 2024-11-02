package com.example.moum.utils;


import java.util.HashMap;

public class ValueMap {

    public static final HashMap<String, Validation> codeToVal = new HashMap<String, Validation>() {
        {
            /**
             * TO-DO 승우님과 코드 맞춰봐야함, 아직까지는 유효하지 않은 코드들임
             */
            put("0000", Validation.VALID_ALL);
            put("E002", Validation.EMAIL_ALREADY_AUTH);
            put("E003", Validation.EMAIL_CODE_FAILED);
            put("E004", Validation.EMAIL_CODE_NOT_CORRECT);
            put("A001", Validation.LOGIN_FAILED);
            put("C002", Validation.EMAIL_NOT_FORMAL);
            put("E001", Validation.VALID_ALL);
            put("C006", Validation.EMAIL_CODE_FAILED);
            put("M002", Validation.VALID_ALL);
            put("S-CH001", Validation.CHAT_POST_SUCCESS);
            put("F-CH001", Validation.CHAT_POST_FAIL);
            put("F-CH002", Validation.CHAT_RECEIVE_FAIL);
        }
    };

    public static Validation getCodeToVal(String code){
        return codeToVal.get(code);
    }
}