package com.example.moum.repository.client;

public enum BaseUrl {
    BASIC_SERVER_PATH("http://223.130.162.175:8080/"),
    CHAT_SERVER_PATH("http://223.130.162.175:8070/");

    private String url;

    BaseUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}