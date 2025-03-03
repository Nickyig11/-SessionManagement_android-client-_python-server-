package com.example.proyectandroidpy;

public class SessionData {
    private String sessionId; //ID unico de la sesion
    private String action; // "entrada", "salida", "incidencia"
    private String details; // Detalles de la incidencia (si aplica)

    public SessionData(String sessionId, String action, String details) {
        this.sessionId = sessionId;
        this.action = action;
        this.details = details;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}