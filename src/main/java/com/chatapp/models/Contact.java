package com.chatapp.models;

/**
 * Modelo de Contacto - Representa la relación entre usuarios
 * Similar a WhatsApp: agregas contactos por nombre de usuario
 */
public class Contact {
    private String ownerUsername; // Usuario que tiene este contacto
    private String contactUsername; // Usuario que fue agregado como contacto
    private long addedAt; // Timestamp de cuando se agregó

    public Contact() {
    }

    public Contact(String ownerUsername, String contactUsername) {
        this.ownerUsername = ownerUsername;
        this.contactUsername = contactUsername;
        this.addedAt = System.currentTimeMillis();
    }

    // Getters
    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getContactUsername() {
        return contactUsername;
    }

    public long getAddedAt() {
        return addedAt;
    }

    // Setters
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }
}
