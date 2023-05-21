package com.business.server;

import com.business.client.controller.ClientController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Clase principal que representa el servidor de chat.
 */
public class ChatServer {

    /**
     * Puerto en el que se iniciará el servidor.
     */
    private static final int PORT = 5000;

    /**
     * Conjunto de usuarios conectados al servidor.
     */
    private Set<String> connectedUsers = new HashSet<>();

    /**
     * Lista de controladores de clientes conectados al servidor.
     */
    private List<ClientController> connectedClients = new ArrayList<>();

    /**
     * Inicia el servidor y acepta conexiones de clientes.
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Crea un nuevo hilo para manejar la comunicación con el cliente
                ClientController clientHandler = new ClientController(clientSocket);
                connectedClients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja una solicitud de conexión del cliente.
     *
     * @param request solicitud del cliente.
     */
    public void handleConnectionRequest(String request) {
        String[] parts = request.split("\\|"); // Dividir la solicitud en base al carácter '|'
        if (parts.length >= 2) {
            String code = parts[0];
            String username = parts[1];

            switch (code) {
                case "01":
                    establishConnection(username);
                    break;
                case "02":
                    confirmConnection(username);
                    break;
                case "03":
                    sendUserList();
                    break;
                case "04":
                    requestUserList(username);
                    break;
                case "05":
                    if (parts.length >= 3) {
                        String message = parts[2];
                        sendMessage(message);
                        saveMessageToXML(username, message);
                    }
                    break;
                case "06":
                    if (parts.length >= 3) {
                        String filePath = parts[2];
                        sendFile(username, filePath);
                        saveFileToXML(username, filePath);
                    }
                    break;
                case "07":
                    closeSession(username);
                    break;
                default:
                    System.out.println("Código desconocido");
                    break;
            }
        } else {
            System.out.println("Solicitud inválida");
        }
    }

    /**
     * Establece una conexión para el usuario dado.
     *
     * @param username nombre de usuario.
     */
    private void establishConnection(String username) {
        if (connectedUsers.contains(username)) {
            System.out.println("01|" + username);
        } else {
            connectedUsers.add(username);
            System.out.println("01|" + username);
        }
    }

    /**
     * Confirma la conexión para el usuario dado.
     *
     * @param username nombre de usuario.
     */
    private void confirmConnection(String username) {
        if (connectedUsers.contains(username)) {
            System.out.println("02|" + username + "|No conectado");
        } else {
            connectedUsers.add(username);
            System.out.println("02|" + username + "|Conectado");
        }
    }

    /**
     * Envía la lista de usuarios conectados a todos los clientes.
     */
    private void sendUserList() {
        // Construir la lista de usuarios conectados
        List<String> userList = new ArrayList<>(connectedUsers);

        // Convertir la lista en un mensaje de texto separado por |
        String userListMessage = String.join("|", userList);

        // Enviar la lista de usuarios a todos los clientes conectados
        for (ClientController client : connectedClients) {
            client.sendMessage("03|" + userListMessage);
        }
        System.out.println("03|" + userListMessage);
    }

    /**
     * Solicita la lista de usuarios conectados al cliente dado.
     *
     * @param username nombre de usuario del cliente.
     */
    private void requestUserList(String username) {
        // Obtener la lista de usuarios conectados
        List<String> userList = new ArrayList<>(connectedUsers);

        // Verificar si el usuario solicitante está en la lista
        if (userList.contains(username)) {
            // Remover al usuario solicitante de la lista
            userList.remove(username);

            // Convertir la lista en un mensaje de texto separado por |
            String userListMessage = String.join("|", userList);

            // Enviar la lista de usuarios al cliente solicitante
            for (ClientController client : connectedClients) {
                if (client.getUsername().equals(username)) {
                    client.sendMessage("04|" + userListMessage);
                    break;
                }
            }
        } else {
            System.out.println("Usuario no encontrado: " + username);
        }
    }

    /**
     * Envía un mensaje a todos los clientes conectados.
     *
     * @param message mensaje a enviar.
     */
    private void sendMessage(String message) {
        // Envía el mensaje a todos los clientes conectados
        for (ClientController client : connectedClients) {
            client.sendMessage("05|" + message);
        }
    }

    /**
     * Envía un archivo al cliente dado.
     *
     * @param username nombre de usuario del cliente.
     * @param filePath ruta del archivo a enviar.
     */
    private void sendFile(String username, String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println("El archivo no existe o no es válido: " + filePath);
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            // Envía el mensaje al cliente para indicar que se enviará un archivo
            String message = "06|" + username + "|" + file.getName();
            byte[] messageBytes = message.getBytes();

            // Envía el tamaño del mensaje al cliente
            for (ClientController client : connectedClients) {
                client.sendMessage("06|" + messageBytes.length);
            }

            // Envía el mensaje a todos los clientes
            for (ClientController client : connectedClients) {
                client.sendBytes(messageBytes);
            }

            // Lee y envía los datos del archivo
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                for (ClientController client : connectedClients) {
                    client.sendBytes(buffer, bytesRead);
                }
            }

            System.out.println("Archivo enviado exitosamente: " + filePath);
        } catch (IOException e) {
            System.out.println("Error al enviar el archivo: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión del usuario dado.
     *
     * @param username nombre de usuario.
     */
    private void closeSession(String username) {
        if (connectedUsers.contains(username)) {
            connectedUsers.remove(username);
            System.out.println("07|" + username + "|Sesión cerrada");

            // Notificar a los demás clientes sobre la desconexión
            for (ClientController client : connectedClients) {
                if (!client.getUsername().equals(username)) {
                    client.sendMessage("07|" + username + "|Sesión cerrada");
                }
            }
        } else {
            System.out.println("07|" + username + "|Usuario no encontrado");
        }
    }

    /**
     * Guarda un mensaje en formato XML para el usuario dado.
     *
     * @param username nombre de usuario.
     * @param message  mensaje a guardar.
     */
    private void saveMessageToXML(String username, String message) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Analizar el archivo XML existente
            File xmlFile = new File("chat.xml");
            Document doc;
            if (xmlFile.exists()) {
                doc = docBuilder.parse(xmlFile);
            } else {
                doc = docBuilder.newDocument();
                Element chatBusiness = doc.createElement("chatBusiness");
                doc.appendChild(chatBusiness);
            }

            // Obtener el elemento raíz
            Element rootElement = doc.getDocumentElement();

            // Encuentre el elemento de usuario con el nombre de usuario dado, o cree uno nuevo si no lo encuentra.
            Element userElement = findOrCreateUserElement(doc, rootElement, username);

            // Crear un nuevo elemento de mensaje
            Element messageElement = doc.createElement("mensaje");

            // Cree el elemento de texto y establezca el texto del mensaje.
            Element textElement = doc.createElement("texto");
            textElement.appendChild(doc.createTextNode(message));
            messageElement.appendChild(textElement);

            // Cree el elemento hora y establezca la marca de tiempo actual
            Element horaElement = doc.createElement("hora");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String timestamp = dateFormat.format(new Date());
            horaElement.appendChild(doc.createTextNode(timestamp));
            messageElement.appendChild(horaElement);

            // Agregar el elemento de mensaje al elemento de usuario
            userElement.getElementsByTagName("chat").item(0).appendChild(messageElement);

            // Vuelva a escribir el XML actualizado en el archivo
            writeXmlToFile(doc, xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda la información de un archivo en formato XML para el usuario dado.
     *
     * @param username nombre de usuario.
     * @param filePath ruta del archivo.
     */
    private void saveFileToXML(String username, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Analizar el archivo XML existente
            File xmlFile = new File("chat.xml");
            Document doc;
            if (xmlFile.exists()) {
                doc = docBuilder.parse(xmlFile);
            } else {
                doc = docBuilder.newDocument();
                Element chatBusiness = doc.createElement("chatBusiness");
                doc.appendChild(chatBusiness);
            }

            // Obtener el elemento raíz
            Element rootElement = doc.getDocumentElement();

            // Encuentre el elemento de usuario con el nombre de usuario dado, o cree uno nuevo si no lo encuentra
            Element userElement = findOrCreateUserElement(doc, rootElement, username);

            // Crear un nuevo elemento de archivo
            Element fileElement = doc.createElement("archivo");

            // Crea el elemento nombre y establece el nombre del archivo.
            Element nombreElement = doc.createElement("nombre");
            File file = new File(filePath);
            nombreElement.appendChild(doc.createTextNode(file.getName()));
            fileElement.appendChild(nombreElement);

            // Cree el elemento ruta y configure la ruta del archivo
            Element rutaElement = doc.createElement("ruta");
            rutaElement.appendChild(doc.createTextNode(filePath));
            fileElement.appendChild(rutaElement);

            //Cree el elemento hora y establezca la marca de tiempo actual
            Element horaElement = doc.createElement("hora");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            horaElement.appendChild(doc.createTextNode(timestamp));
            fileElement.appendChild(horaElement);

            // Agregue el elemento de archivo al elemento de usuario
            userElement.getElementsByTagName("archivos").item(0).appendChild(fileElement);

            // Vuelva a escribir el XML actualizado en el archivo
            writeXmlToFile(doc, xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca el elemento de usuario con el nombre de usuario dado en el documento XML.
     * Si no se encuentra, crea un nuevo elemento de usuario.
     *
     * @param doc          documento XML.
     * @param rootElement  elemento raíz del documento XML.
     * @param username     nombre de usuario.
     * @return elemento de usuario encontrado o creado.
     */
    private Element findOrCreateUserElement(Document doc, Element rootElement, String username) {
        NodeList userList = rootElement.getElementsByTagName("usuario");
        for (int i = 0; i < userList.getLength(); i++) {
            Element userElement = (Element) userList.item(i);
            Element nombreElement = (Element) userElement.getElementsByTagName("nombre").item(0);
            String name = nombreElement.getTextContent();
            if (name.equals(username)) {
                return userElement;
            }
        }

        // Si no se encuentra el elemento de usuario, cree uno nuevo
        Element userElement = doc.createElement("usuario");

        // Crea el elemento nombre y establece el nombre de usuario
        Element nombreElement = doc.createElement("nombre");
        nombreElement.appendChild(doc.createTextNode(username));
        userElement.appendChild(nombreElement);

        // Cree el elemento tipo y establezca un valor predeterminado (puede modificarlo según sea necesario)
        Element tipoElement = doc.createElement("tipo");
        tipoElement.appendChild(doc.createTextNode("default"));
        userElement.appendChild(tipoElement);

        // Crear el elemento de chat
        Element chatElement = doc.createElement("chat");
        userElement.appendChild(chatElement);

        // Crear el elemento archivos
        Element archivosElement = doc.createElement("archivos");
        userElement.appendChild(archivosElement);

        // Agregar el elemento de usuario al elemento raíz
        rootElement.appendChild(userElement);

        return userElement;
    }

    /**
     * Escribe el documento XML actualizado en el archivo XML dado.
     *
     * @param doc  documento XML actualizado.
     * @param file archivo XML.
     * @throws IOException en caso de error de escritura.
     */
    private void writeXmlToFile(Document doc, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Usa un Transformer para la salida
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
