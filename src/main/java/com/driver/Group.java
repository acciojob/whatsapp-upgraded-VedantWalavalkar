package com.driver;

import java.util.List;

public class Group {
    private String name;
    private int numberOfParticipants;

    private List<User> users;

    private List<Message> messages;
    public Group(){
    }

    public Group(String name, int numberOfParticipants) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Group(String name, int numberOfParticipants, List<User> users, List<Message> messages) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.users = users;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
}
