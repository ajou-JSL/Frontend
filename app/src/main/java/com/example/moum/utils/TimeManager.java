package com.example.moum.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class TimeManager {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String strToDate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        try {
            // ISO 형식 처리 (Offset 포함)
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(input, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return offsetDateTime.toLocalDate().toString();
        } catch (DateTimeParseException e) {
            try {
                // ISO 형식 처리 (Offset 없는 LocalDateTime)
                LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return dateTime.toLocalDate().toString();
            } catch (DateTimeParseException ex) {
                try {
                    // 단순 날짜 포맷 (yyyy-MM-dd)
                    LocalDate date = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
                    return date.toString();
                } catch (DateTimeParseException exc) {
                    return "";
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String strToDatetime(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        try {
            // ISO 형식 우선 처리 (Offset 포함)
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(input, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return offsetDateTime.toLocalDateTime().toString();
        } catch (DateTimeParseException e) {
            try {
                // ISO 형식 처리 (Offset 없는 LocalDateTime)
                LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return dateTime.toString();
            } catch (DateTimeParseException ex) {
                return "";
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String strToPrettyTime(String input) {
        /*target 시간과 현재 시간 구하기*/
        LocalDateTime target;
        LocalDateTime now = LocalDateTime.now();
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        try {
            // ISO 형식 처리 (Offset 포함)
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(input, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            target = offsetDateTime.toLocalDateTime();
        } catch (DateTimeParseException e) {
            try {
                // ISO 형식 처리 (Offset 없는 LocalDateTime)
                target = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException ex) {
                return "";
            }
        }

        /*현재시간을 기준으로 반환 시간 설정*/
        long years = ChronoUnit.YEARS.between(target, now);
        long months = ChronoUnit.MONTHS.between(target, now);
        long days = ChronoUnit.DAYS.between(target, now);
        long hours = ChronoUnit.HOURS.between(target, now);
        long minutes = ChronoUnit.MINUTES.between(target, now);
        long seconds = ChronoUnit.SECONDS.between(target, now);

        if (years == 0 && months == 0 && days == 0 && hours == 0 && minutes == 0) {
            return "방금 전";
        } else if (years == 0 && months == 0 && days == 0 && hours == 0) {
            return minutes + "분 전";
        } else if (years == 0 && months == 0 && days == 0) {
            return hours + "시간 전";
        } else if (years == 0 && months == 0) {
            return days + "일 전";
        } else if (years == 0) {
            return months + "달 전";
        } else {
            return years + "년 전";
        }
    }

}
