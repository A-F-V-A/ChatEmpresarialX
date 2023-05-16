package com.business.client.model;

public class User {
    private String id;
    private String lastName;
    private String firstName;
    private Role role;
    private Chat chat;

    public User(String lastName, String firstName, Role role, Chat chat, String id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.chat = chat;
        this.id = id;
    }

    public User(String lastName, String firstName, Role role, String id) {
        this(lastName, firstName, role,null, id);
    }

    public User(String lastName, String firstName, String id) {
        this(lastName, firstName, null, null,id);
    }

    public User(String lastName, String firstName) {
        this(lastName, firstName, null, null,null);
    }

    // Getter and Setter for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and Setter for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and Setter for role
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Getter and Setter for chat
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
