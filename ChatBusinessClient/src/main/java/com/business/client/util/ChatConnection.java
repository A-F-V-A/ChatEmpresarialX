package com.business.client.util;

import com.business.client.model.Chat;
import com.business.client.model.CommunicationCode;

import java.io.*;
import java.net.Socket;

public class ChatConnection {
    private Socket connection;
    private BufferedReader reader;
    private PrintWriter writer;


    public ChatConnection(String serverAddress, int port) throws IOException {
        connection = new Socket(serverAddress,port);
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        writer = new PrintWriter(connection.getOutputStream(), true);
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

}
