package com.business.client.model;


import java.util.ArrayList;
import java.util.List;


public class Chat {

    private int port;
    private String serverAddress;
    private String nickname;
    private List<Message> messageList;
    private List<File> fileList;
    private List<String> userList;

    public Chat(int port, String serverAddress, String nickname) {
        this.port = port;
        this.serverAddress = serverAddress;
        this.nickname = nickname;
        messageList = new ArrayList<>();
        fileList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public void addFile(File file) {
        fileList.add(file);
    }

    public void addUser(String nickname) {
        userList.add(nickname);
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public int getPort() {
        return port;
    }

    public String getServerAddress() {
        return serverAddress;
    }
}