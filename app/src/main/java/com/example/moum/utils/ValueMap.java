package com.example.moum.utils;


import java.util.HashMap;

public class ValueMap {

    public static final HashMap<String, Validation> codeToVal = new HashMap<String, Validation>() {
        {
            /**
             * TO-DO 승우님과 코드 맞춰봐야함, 아직까지는 유효하지 않은 코드들임
             */
            put("0000", Validation.VALID_ALL);
            put("E001", Validation.EMAIL_NOT_FORMAL);
            put("E002", Validation.EMAIL_ALREADY_AUTH);
            put("E003", Validation.EMAIL_CODE_FAILED);
            put("E004", Validation.EMAIL_CODE_NOT_CORRECT);
            put("A004", Validation.LOGIN_FAILED);
        }
    };

    public static Validation getCodeToVal(String code){
        return codeToVal.get(code);
    }
}