package com.chatapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo de Mensaje para la aplicación de chat
 */
public class Message {
    private String from;
    private String to;
    private String content;
    private String contentType; // text, emoji, image
    private String timestamp;
    private String groupId; // null para mensajes privados
    private boolean read; // Para indicar si fue leído

    /**
     * Constructor para mensajes privados
     */
    public Message(String from, String to, String content, String contentType) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.contentType = contentType;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.groupId = null;
        this.read = false;
    }

    /**
     * Constructor para mensajes de grupo
     */
    public Message(String from, String groupId, String content, String contentType, boolean isGroup) {
        this.from = from;
        this.groupId = groupId;
        this.content = content;
        this.contentType = contentType;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.to = null;
        this.read = isGroup;
    }

    // Getters
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getGroupId() {
        return groupId;
    }

    // Setters
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
