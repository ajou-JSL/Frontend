package com.example.moum.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(formatter)); // LocalDateTime을 ISO 8601 String으로 직렬화
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) { // JSON에서 null 확인
            in.nextNull(); // null 소비
            return null; // null로 매핑
        }

        String dateTimeString = in.nextString(); // JSON에서 String 읽기
        try {
            return LocalDateTime.parse(dateTimeString, formatter); // String -> LocalDateTime 변환
        } catch (Exception e) {
            throw new IOException("Invalid LocalDateTime format: " + dateTimeString, e); // 잘못된 형식 처리
        }
    }
}
