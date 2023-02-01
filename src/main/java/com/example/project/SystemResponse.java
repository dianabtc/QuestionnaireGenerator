package com.example.project;

public class SystemResponse {
    private final String status;
    private final String message;

    public SystemResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "'status' :'" + status + '\'' +
                ", 'message' : '" + message + '\'' +
                '}';
    }
}
