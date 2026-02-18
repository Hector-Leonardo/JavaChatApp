package com.chatapp.models;

/**
 * Modelo de Usuario para la aplicación de chat
 */
public class User {
    private String username;
    private String password;
    private boolean online;
    private String profilePicture;
    private Long lastSeen;
    private String status;
    private String about;

    /**
     * Constructor de User
     * 
     * @param username Nombre de usuario único
     * @param password Contraseña del usuario
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.online = false;
        this.profilePicture = null;
        this.lastSeen = null;
        this.status = "";
        this.about = "";
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOnline() {
        return online;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public String getStatus() {
        return status;
    }

    public String getAbout() {
        return about;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", online=" + online +
                ", profilePicture='" + profilePicture + '\'' +
                ", lastSeen=" + lastSeen +
                ", status='" + status + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
