package com.business.client.util;

import com.business.client.model.Chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatConnection {
    public static void sendData(String data, Chat chat) throws IOException {
        Socket connection = new Socket(chat.getServerAddress(), chat.getPort());
        DataOutputStream outputConnection = new DataOutputStream(connection.getOutputStream());
        outputConnection.writeUTF(data);
        outputConnection.flush();
        outputConnection.close();
        System.out.println("Send Message");
    }
}
