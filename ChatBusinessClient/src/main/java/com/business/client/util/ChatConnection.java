package com.business.client.util;

import com.business.client.controller.ChatController;
import com.business.client.model.Chat;
import com.business.client.model.ChatFile;
import com.business.client.model.CommunicationCode;
import com.business.client.model.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa la conexión de chat con el servidor.
 */
public class ChatConnection {
    private Socket connection;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread listenMessage;
    private ChatController drawMessage;

    /**
     * Constructor de la clase ChatConnection que establece la conexión con el servidor.
     *
     * @param serverAddress Dirección del servidor.
     * @param port          Puerto del servidor.
     * @throws IOException Si ocurre un error de E/S durante la conexión.
     */
    public ChatConnection(String serverAddress, int port) throws IOException {
        connection = new Socket(serverAddress,port);
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        writer = new PrintWriter(connection.getOutputStream(), true);
    }

    /**
     * Establece el controlador de chat para dibujar mensajes.
     *
     * @param drawMessage Controlador de chat que dibuja mensajes.
     */
    public void setDrawMessage(ChatController drawMessage) {
        this.drawMessage = drawMessage;
    }




    /**
     * Establece la conexión con el servidor enviando un mensaje de conexión y recibiendo una respuesta.
     *
     * @param message Mensaje de conexión.
     * @return true si la conexión fue establecida correctamente, false de lo contrario.
     */
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

    /**
     * Cierra la conexión con el servidor enviando un mensaje de desconexión.
     *
     * @param message Mensaje de desconexión.
     */
    public void disconnect(String message) {
        writer.println(message);
        System.out.println(message);
        close();
        stopListening();
    }

    /* Métodos de conexión */

    /**
     * Envía un mensaje al servidor.
     *
     * @param message Mensaje a enviar.
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * Recibe un mensaje del servidor.
     *
     * @return El mensaje recibido.
     * @throws IOException Si ocurre un error de E/S durante la lectura del mensaje.
     */
    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    /**
     * Cierra la conexión con el servidor y libera los recursos.
     */
    public void close() {
        try {
            writer.close();
            reader.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia un hilo para escuchar los mensajes del servidor.
     */
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

    /**
     * Detiene la escucha de mensajes del servidor.
     */
    public void stopListening() {
        if (listenMessage != null) {
            listenMessage.interrupt();
        }
    }

    /**
     * Analiza el mensaje recibido del servidor y realiza acciones correspondientes en función del código del mensaje.
     *
     * @param message El mensaje recibido del servidor.
     */
    private void  typeMessage(String message){
        if (message == null) {
            System.out.println("Se ha desconectado por inactividad");
        }

        String[] parts = message.split("\\|");
        int code = Integer.parseInt(parts[0]);
        switch (code) {
            case 3:
                // Mensaje de lista de usuarios conectados
                List<String> arrayList = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
                drawMessage.loggedInUser(arrayList);
                break;

            case 5:
                // Mensaje de chat
                String nickName = parts[1];
                String chatMessage = parts[2];
                String timestamp = parts[3];
                Date date = new Date(timestamp);
                Message newMessage = new Message(nickName,chatMessage,date);
                drawMessage.DrawMessage(newMessage);
                break;

            case 6:
                // Mensaje de archivo
                String nickName01 = parts[1];
                String timestamp01 = parts[2];
                String fileType = parts[3];
                String fileContent = parts[4];

                Date date01 = new Date(timestamp01);
                ChatFile newChatFile = new ChatFile(nickName01,date01,fileContent,fileType);

                drawMessage.DrawMessageFile(newChatFile);

                break;

            case 7:
                drawMessage.closeConnectionForce();
                break;

            default:
                // Código de mensaje inválido
                break;
        }
    }
}
