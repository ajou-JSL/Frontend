package com.example.moum.utils;

import com.example.moum.data.entity.Genre;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
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
        int value = in.nextInt();
        for (Genre genre : Genre.values()) {
            if (genre.getValue() == value) {
                return genre;
            }
        }
        return null;
    }
}