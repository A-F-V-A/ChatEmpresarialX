package com.business.client.model;


import java.util.ArrayList;
import java.util.List;


public class Chat {

    private int port;
    private String serverAddress;
    private String nickname;
    private List<Message> messageList;
    private List<ChatFile> chatFileList;
    private List<String> userList;

    public Chat(int port, String serverAddress, String nickname) {
        this.port = port;
        this.serverAddress = serverAddress;
        this.nickname = nickname;
        messageList = new ArrayList<>();
        chatFileList = new ArrayList<>();
        userList = new ArrayList<>();
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

    public String getServerAddress() {
        return serverAddress;
    }

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