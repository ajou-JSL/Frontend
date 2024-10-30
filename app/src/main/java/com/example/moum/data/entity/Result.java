package com.example.moum.data.entity;

import com.example.moum.utils.Validation;

public class Result<T> {

    private Validation validation;
    private T data;

    public Result(Validation validation){
        this.validation = validation;
    }
    public Result(Validation validation, T data){
        this.validation = validation;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }
}
