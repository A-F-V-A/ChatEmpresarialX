package com.business.server.managers;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase UserManager gestiona la información de los usuarios conectados al chat.
 * Proporciona métodos para verificar la disponibilidad de un nombre de usuario,
 * agregar usuarios, eliminar usuarios y obtener la lista de usuarios conectados.
 */
public class UserManager {
    private List<String> connectedUsers;

    /**
     * Construye un nuevo objeto UserManager.
     * Inicializa la lista de usuarios conectados.
     */
    public UserManager() {
        connectedUsers = new ArrayList<>();
    }

    /**
     * Verifica si un nombre de usuario está disponible.
     *
     * @param nickname el nombre de usuario a verificar
     * @return true si el nombre de usuario está disponible, false de lo contrario
     */
    public synchronized boolean isNicknameAvailable(String nickname) {
        return !connectedUsers.contains(nickname);
    }

    /**
     * Agrega un nuevo usuario a la lista de usuarios conectados.
     *
     * @param nickname el nombre de usuario a agregar
     * @return true si el usuario se agregó correctamente, false si el nombre de usuario ya está en uso
     */
    public synchronized boolean addUser(String nickname) {
        if (isNicknameAvailable(nickname)) {
            connectedUsers.add(nickname);
            return true;
        }
        return false;
    }

    /**
     * Elimina un usuario de la lista de usuarios conectados.
     *
     * @param nickname el nombre de usuario a eliminar
     */
    public synchronized void removeUser(String nickname) {
        connectedUsers.remove(nickname);
    }

    /**
     * Obtiene una copia de la lista de usuarios conectados.
     *
     * @return una lista de usuarios conectados
     */
    public synchronized List<String> getUserList() {
        return new ArrayList<>(connectedUsers);
    }

    /**
     * Genera un mensaje de lista de usuarios para ser enviado a los clientes.
     *
     * @return un mensaje de lista de usuarios en el formato adecuado
     */
    public synchronized String getUserListMessage() {
        StringBuilder userListMessage = new StringBuilder("03|");
        for (String user : connectedUsers) {
            userListMessage.append(user).append("|");
        }
        userListMessage.deleteCharAt(userListMessage.length() - 1); // Eliminar el último separador "|"
        return userListMessage.toString();
    }

}
