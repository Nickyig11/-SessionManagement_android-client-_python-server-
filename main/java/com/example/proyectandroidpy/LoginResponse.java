package com.example.proyectandroidpy;

public class LoginResponse {
    private boolean success;
    private String message;
    private String sessionId;
    private boolean admin;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
