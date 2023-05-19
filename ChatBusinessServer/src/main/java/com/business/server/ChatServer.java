package com.business.server;

import com.business.client.controller.ClientController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static final int PORT = 5000; // Puerto en el que escucha el servidor

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Crea un nuevo hilo para manejar la comunicación con el cliente
                ClientController clientHandler = new ClientController(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Crea un nuevo hilo para manejar la comunicación con el cliente
                ClientController clientHandler = new ClientController(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConnectionRequest(String username, String code){


        switch (code){
            case "01":
                establishConnection(username);
                break;
            case "02":
                confirmConnection(username);
                break;
            case "03":
                sendUserList(username);
                break;
            case "04":
                requestUserList(username);
                break;
            case "05":
                sendMessage(username);
                break;
            case "06":
                sendFile(username);
                break;
            case "07":
                closeSession(username);
                break;
            default:
                System.out.println("Código desconocido");
                break;
        }
    }
    private void establishConnection(String username) {
    }

    private void confirmConnection(String username) {
    }

    private void sendUserList(String username) {
        // Lógica para enviar la lista de usuarios conectados
    }

    private void requestUserList(String username) {
        // Lógica para solicitar la lista de usuarios conectados
    }

    private void sendMessage(String username) {
        // Aquí implementa la lógica para enviar un mensaje
    }

    private void sendFile(String username) {
        // Aquí implementa la lógica para enviar un archivo
    }

    private void closeSession(String username) {
        // Lógica para cerrar la sesión del usuario
    }


}
