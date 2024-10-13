package com.example.moum.data.entity;

public class User {
    private String name;
    private String id;
    private String password;
    private String passwordCheck;
    private String email;


    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public String getPasswordCheck() {
        return passwordCheck;
    }
    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
