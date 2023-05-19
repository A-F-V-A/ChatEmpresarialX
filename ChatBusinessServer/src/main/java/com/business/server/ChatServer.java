package com.business.server;

import com.business.client.controller.ClientController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 5000; // Puerto en el que escucha el servidor
    private Set<String> connectedUsers = new HashSet<>();
    private List<ClientController> connectedClients = new ArrayList<>();

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
        System.out.println("01|" + username);
    }

    private void confirmConnection(String username) {
        if (connectedUsers.contains(username)) {
            System.out.println("02|" + username + "|No conectado");

        } else {
            connectedUsers.add(username);
            System.out.println("02|" + username + "|Conectado");
        }
    }

    private void sendUserList(String username) {
        // Construir la lista de usuarios conectados
        List<String> userList = new ArrayList<>(connectedUsers);

        // Convertir la lista en un mensaje de texto separado por |
        String userListMessage = String.join("|", userList);

        System.out.println("03|" + userListMessage);
    }

    private void requestUserList(String username) {
        // Comunicación cliente-servidor
    }

    private void sendMessage(String message) {
        for (ClientController client : connectedClients) {
            client.sendMessage(message);
        }
    }

    private void sendFile(String username) {
        // Aquí implementa la lógica para enviar un archivo
    }

    private void closeSession(String username) {
        // Lógica para cerrar la sesión del usuario
    }


}
