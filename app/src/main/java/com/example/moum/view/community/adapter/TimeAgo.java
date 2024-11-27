package com.example.moum.view.community.adapter;

import android.os.Build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.Duration;

public class TimeAgo {
    public static String getTimeAgo(String createdAt) {
        if (createdAt == null || createdAt.isEmpty()) {
            return "알 수 없음";  // null 또는 빈 문자열일 경우 처리
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return "기능 지원 안됨"; // API 26 미만에서는 지원하지 않음
        }

        // ISO 8601 형식 문자열을 LocalDateTime으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime createdDateTime;
        try {
            createdDateTime = LocalDateTime.parse(createdAt, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return "잘못된 날짜 형식";  // 날짜 형식이 잘못된 경우 처리
        }

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 두 시간 간의 차이 계산
        Duration duration = Duration.between(createdDateTime, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            return duration.toMinutes() + "분 전";
        } else if (seconds < 86400) { // 86400초 = 1일
            return duration.toHours() + "시간 전";
        } else if (seconds < 2592000) { // 2592000초 = 30일
            return duration.toDays() + "일 전";
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return createdDateTime.format(dateFormatter);
        }
    }
}
