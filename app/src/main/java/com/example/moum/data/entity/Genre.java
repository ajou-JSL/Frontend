package com.example.moum.data.entity;

import androidx.annotation.NonNull;

public enum Genre {
    POP(0),
    ROCK(1),
    JAZZ(2),
    CLASSICAL(3),
    HIP_HOP(4),
    RNB(5),
    EDM(6),
    BLUES(7),
    COUNTRY(8),
    METAL(9),
    REGGAE(10),
    SOUL(11),
    FUNK(12),
    INDIE(13),
    FOLK(14);

    private final int value;
    Genre(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Genre fromValue(int value) {
        for (Genre type : Genre.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    public static String[] toStringArray() {
        Genre[] genres = Genre.values();
        String[] genreNames = new String[genres.length];
        for (int i = 0; i < genres.length; i++) {
            genreNames[i] = genres[i].name();
        }
        return genreNames;
    }

    public static Genre fromString(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        }
        try {
            return Genre.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown Genre name: " + name);
        }
    }

    public static String fromInt(int value) {
        for (Genre genre : Genre.values()) {
            if (genre.getValue() == value) {
                return genre.name(); // enum 이름을 반환
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}
