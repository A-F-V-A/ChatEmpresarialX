package com.business.client.util;

import com.business.client.model.Chat;
import com.business.client.model.CommunicationCode;

import java.io.*;
import java.net.Socket;

public class ChatConnection {
    private Socket connection;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread listenMessage;


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

    public void disconnect(String message) {
        writer.println(message);
        System.out.println(message);
        close();
    }

    public void Hear() {
        listenMessage = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String receivedMessage = receiveMessage();
                    // Aquí puedes procesar la información recibida del servidor como desees
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
