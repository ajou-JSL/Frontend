package com.example.moum.data.entity;

public class Music {
    private String musicName;
    private String artistName;

    public Music(String musicName, String artistName) {
        this.musicName = musicName;
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    @Override
    public String toString() {
        return "Music{" +
                "musicName='" + musicName + '\'' +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
