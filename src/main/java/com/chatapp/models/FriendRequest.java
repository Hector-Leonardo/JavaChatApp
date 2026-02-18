package com.chatapp.models;

/**
 * Modelo de Solicitud de Amistad
 * Representa una solicitud pendiente entre dos usuarios
 */
public class FriendRequest {
    private String id; // ID único de la solicitud
    private String fromUsername; // Usuario que envía la solicitud
    private String toUsername; // Usuario que recibe la solicitud
    private String status; // pending, accepted, rejected
    private long createdAt; // Timestamp de creación

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_REJECTED = "rejected";

    public FriendRequest() {
    }

    public FriendRequest(String fromUsername, String toUsername) {
        this.id = fromUsername + "_" + toUsername + "_" + System.currentTimeMillis();
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.status = STATUS_PENDING;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public String getStatus() {
        return status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
