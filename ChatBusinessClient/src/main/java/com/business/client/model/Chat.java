package com.business.client.model;


import com.business.client.util.ChatConnection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Chat {

    private int port;
    private String serverAddress;
    private String nickname;
    private ChatConnection connection;
    private List<Message> messageList;
    private List<ChatFile> chatFileList;
    private List<String> userList;

    public Chat(){}

    public Chat(int port, String serverAddress, String nickname) throws IOException {
        this.port = port;
        this.serverAddress = serverAddress;
        this.nickname = nickname;
        this.messageList = new ArrayList<>();
        this.chatFileList = new ArrayList<>();
        this.userList = new ArrayList<>();

        /* connection */
        this.connection = new ChatConnection(this.serverAddress, this.port);
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public void addFile(ChatFile chatFIle) {
        chatFileList.add(chatFIle);
    }

    public void addUser(String nickname) {
        userList.add(nickname);
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public List<ChatFile> getFileList() {
        return chatFileList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public int getPort() {
        return port;
    }

    public String getNickname() {
        return nickname;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public ChatConnection getConnection() {
        return connection;
    }



    /* Comunicacion basica del chat para el server */
    public String connectSession(){
        return CommunicationCode.ESTABLISH_CONNECTION.getCode() +'|'+ nickname;
    }

    public String requestUserLIst(){
        return CommunicationCode.REQUEST_USER_LIST.getCode() +'|'+ nickname;
    }

    public String signOff(){
        return CommunicationCode.CLOSE_SESSION.getCode() +'|'+ nickname;
    }


}