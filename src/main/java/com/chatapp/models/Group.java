package com.chatapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de Grupo para la aplicación de chat
 */
public class Group {
    private String id;
    private String name;
    private List<String> members;
    private String creator;
    private String timestamp;

    /**
     * Constructor de Grupo
     * 
     * @param id      ID único del grupo
     * @param name    Nombre del grupo
     * @param members Lista de miembros del grupo
     * @param creator Usuario que creó el grupo
     */
    public Group(String id, String name, List<String> members, String creator) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>(members);
        this.creator = creator;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return new ArrayList<>(members);
    }

    public String getCreator() {
        return creator;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<String> members) {
        this.members = new ArrayList<>(members);
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Agregar un miembro al grupo
     */
    public void addMember(String username) {
        if (!members.contains(username)) {
            members.add(username);
        }
    }

    /**
     * Remover un miembro del grupo
     */
    public void removeMember(String username) {
        members.remove(username);
    }

    /**
     * Verificar si un usuario es miembro
     */
    public boolean isMember(String username) {
        return members.contains(username);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", members=" + members.size() +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
