package com.example.moum.data.entity;

public class Signout {
    private String details;
    private String username;

    public String getDetails() {
        return details;
    }

    public String getUsername() {
        return username;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Signout{" +
                "details='" + details + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
