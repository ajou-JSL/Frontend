package com.example.moum.repository;

public interface Callback<T> {
    void onResult(T result);
}