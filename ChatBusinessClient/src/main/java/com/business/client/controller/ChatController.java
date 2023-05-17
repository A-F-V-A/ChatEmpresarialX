package com.business.client.controller;

import com.business.client.model.User;
import com.business.client.view.ChatView;


public class ChatController {

    private User user;
    private ChatView chatView;

    public ChatController(User user, ChatView chatView) {
        this.user = user;
        this.chatView = chatView;
    }

    public ChatController() {
    }

    public void sendMessage(User user, String message) {}



    public void sendFile(String filePath, String fileType) {

    }

    public void disconnect() {

    }

    public void setChatView(ChatView chatView) {
    }

}

