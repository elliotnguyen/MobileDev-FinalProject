package com.example.edupro.model;

public class MessageDao {
    private final String message;
    private final int role;

    public MessageDao(String message, int role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public int getRole() {
        return role;
    }
}
