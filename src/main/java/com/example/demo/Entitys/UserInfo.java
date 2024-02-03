package com.example.demo.Entitys;

public class UserInfo {
    private String username;
    private String password;
    private String emailAdd;
    private boolean state;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public boolean getState() {
        return state;
    }


    public void setState(boolean state) {
        this.state = state;
    }

    private String token;
}
