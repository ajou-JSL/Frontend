package com.example.moum;

public class calculator {

    // 더하기 메서드
    public int add(int a, int b) {
        return a + b;
    }

    // 빼기 메서드
    public int subtract(int a, int b) {
        return a - b;
    }

    // 곱하기 메서드
    public int multiply(int a, int b) {
        return a * b;
    }

    // 나누기 메서드
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        return a / b;
    }
}
