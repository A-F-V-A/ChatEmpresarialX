package com.business.client.util;

import com.business.client.controller.ChatController;
import com.business.client.model.Chat;
import com.business.client.model.CommunicationCode;
import com.business.client.model.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatConnection {
    private Socket connection;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread listenMessage;
    private ChatController drawMessage;

    public ChatConnection(String serverAddress, int port) throws IOException {
        connection = new Socket(serverAddress,port);
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        writer = new PrintWriter(connection.getOutputStream(), true);
    }

    public void setDrawMessage(ChatController drawMessage) {
        this.drawMessage = drawMessage;
    }


    /* Logic */

    public boolean connect(String message) {
        writer.println(message);

        try {
            String response = reader.readLine();

            System.out.println(response);

            return response.equals("2|conectado");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void disconnect(String message) {
        writer.println(message);
        System.out.println(message);
        close();
        stopListening();
    }

    /* metodos de connecion */

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void close() {
        try {
            writer.close();
            reader.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Hilo */
    public void Hear() {
        listenMessage = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String receivedMessage = receiveMessage();
                    typeMessage(receivedMessage);
                    System.out.println("Mensaje recibido: " + receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenMessage.start();
    }

    public void stopListening() {
        if (listenMessage != null) {
            listenMessage.interrupt();
        }
    }

    private void  typeMessage(String message){
        String[] parts = message.split("\\|");
        int code = Integer.parseInt(parts[0]);
        switch (code) {
            case 3:
                List<String> arrayList = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
                drawMessage.loggedInUser(arrayList);
                break;

            case 5:
                // Chat message
                String nickName = parts[1];
                String chatMessage = parts[2];
                String timestamp = parts[3];
                Date date = new Date(timestamp);
                Message newMessage = new Message(nickName,chatMessage,date);
                drawMessage.DrawMessage(newMessage);
                break;

            case 6:
                // File message
                /*
                String fileType = parts[3];
                String fileContent = parts[4];
                String fileMessage = "6|" + sender.getNickname() + "|" + parts[2] + "|" + fileType + "|" + fileContent;
                broadcastMessage(fileMessage,sender);
                messageFileManager.addMessage(sender.getNickname(),fileContent,parts[2],FileType.valueOf(fileType));
                 */
                break;

            default:
                // Invalid message code
                break;
        }
    }
}
