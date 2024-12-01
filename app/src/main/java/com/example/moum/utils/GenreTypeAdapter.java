package com.example.moum.utils;

import com.example.moum.data.entity.Genre;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GenreTypeAdapter extends TypeAdapter<Genre> {
    @Override
    public void write(JsonWriter out, Genre genre) throws IOException {
        if (genre == null) {
            out.nullValue();
        } else {
            out.value(genre.getValue()); // Enum의 숫자 값으로 직렬화
        }
    }

    @Override
    public Genre read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) { // JSON에서 null 확인
            in.nextNull(); // null 소비
            return null; // null로 매핑
        }

        String name = in.nextString(); // JSON에서 String 읽기
        try {
            return Genre.valueOf(name.toUpperCase()); // Enum으로 변환 (대소문자 무시)
        } catch (IllegalArgumentException e) {
            throw new IOException("Unknown Genre: " + name, e); // 알 수 없는 값 처리
        }
    }
}