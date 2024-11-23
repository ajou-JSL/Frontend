package com.example.moum.view.community.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.Duration;

public class TimeAgo {
    public static String getTimeAgo(String createdAt) {
        // ISO 8601 형식 문자열을 LocalDateTime으로 변환
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ISO_DATE_TIME;
        }
        LocalDateTime createdDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createdDateTime = LocalDateTime.parse(createdAt, formatter);
        }

        // 현재 시간
        LocalDateTime now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }

        // 두 시간 간의 차이 계산
        Duration duration = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            duration = Duration.between(createdDateTime, now);
        }

        long seconds = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            seconds = duration.getSeconds();
        }

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            long minutes = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                minutes = duration.toMinutes();
            }
            return minutes + "분 전";
        } else if (seconds < 86400) { // 86400초 = 1일
            long hours = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                hours = duration.toHours();
            }
            return hours + "시간 전";
        } else if (seconds < 2592000) { // 2592000초 = 30일
            long days = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                days = duration.toDays();
            }
            return days + "일 전";
        } else {
            return "오래됨";
        }
    }
}
