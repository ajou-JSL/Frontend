package com.example.moum.data.entity;

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
}
