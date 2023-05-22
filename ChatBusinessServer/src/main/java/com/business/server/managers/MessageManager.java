package com.business.server.managers;
import com.business.server.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageManager {
    private List<Message> messageList;

    public MessageManager() {
        messageList = new ArrayList<>();
    }

    public synchronized void addMessage(String sender, String content, String  timestamp) {
        Date date = new Date(timestamp);
        Message message = new Message(sender, content, date);
        System.out.println(message.toString());
        messageList.add(message);
    }

    public synchronized List<Message> getMessageList() {
        return new ArrayList<>(messageList);
    }
}