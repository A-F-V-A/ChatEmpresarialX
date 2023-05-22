package com.business.server.managers;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<String> connectedUsers;

    public UserManager() {
        connectedUsers = new ArrayList<>();
    }

    public synchronized boolean isNicknameAvailable(String nickname) {
        return !connectedUsers.contains(nickname);
    }

    public synchronized boolean addUser(String nickname) {
        if (isNicknameAvailable(nickname)) {
            connectedUsers.add(nickname);
            return true;
        }
        return false;
    }

    public synchronized void removeUser(String nickname) {
        connectedUsers.remove(nickname);
    }

    public synchronized List<String> getUserList() {
        return new ArrayList<>(connectedUsers);
    }
    public synchronized String getUserListMessage() {
        StringBuilder userListMessage = new StringBuilder("03|");
        for (String user : connectedUsers) {
            userListMessage.append(user).append("|");
        }
        userListMessage.deleteCharAt(userListMessage.length() - 1); // Eliminar el último separador "|"
        return userListMessage.toString();
    }

}
