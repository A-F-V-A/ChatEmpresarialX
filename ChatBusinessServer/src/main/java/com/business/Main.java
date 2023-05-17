package com.business;

import com.business.server.ChatServer;

public class Main {
    public static void main(String[] args) {

        ChatServer server = new ChatServer();
        server.start();
    }
}