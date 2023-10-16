package com.example.sales_management.domain;

public class Agent {

    private String username;

    private String password;

    public Agent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Agent() {

    }

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

}
