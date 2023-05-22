package com.business.server.util;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;

public class XMLFileManager {

    private String archivoXML;

    public XMLFileManager(String archivoXML) {
        this.archivoXML = archivoXML;
    }

    /**
     * Agrega una conexión al archivo XML.
     *
     * @param username  El nombre de usuario de la conexión.
//     * @param timestamp El timestamp de la conexión.
     */
    public void agregarConexion(String username) {
        try {
            Document document = cargarDocumentoXML();

            Element connectionsElement = document.getRootElement().getChild("connections");

            Element connectionElement = new Element("connection");
            connectionElement.addContent(new Element("username").setText(username));
//            connectionElement.addContent(new Element("timestamp").setText(timestamp));

            connectionsElement.addContent(connectionElement);

            guardarDocumentoXML(document);

            System.out.println("Conexión agregada exitosamente al archivo XML.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un mensaje al archivo XML.
     *
     * @param username    El nombre de usuario del mensaje.
     * @param messageUser El mensaje del usuario.
     * @param timestamp   El timestamp del mensaje.
     */
    public void agregarMensaje(String username, String messageUser, String timestamp) {
        try {
            Document document = cargarDocumentoXML();

            Element messagesElement = document.getRootElement().getChild("messages");

            Element messageElement = new Element("message");
            messageElement.addContent(new Element("username").setText(username));
            messageElement.addContent(new Element("messageuser").setText(messageUser));
            messageElement.addContent(new Element("timestamp").setText(timestamp));

            messagesElement.addContent(messageElement);

            guardarDocumentoXML(document);

            System.out.println("Mensaje agregado exitosamente al archivo XML.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un archivo al archivo XML.
     *
     * @param username El nombre de usuario del archivo.
     * @param fileUser El nombre del archivo.
     * @param timestamp El timestamp del archivo.
     */
    public void agregarArchivo(String username, String fileUser, String timestamp) {
        try {
            Document document = cargarDocumentoXML();

            Element filesElement = document.getRootElement().getChild("files");

            Element fileElement = new Element("file");
            fileElement.addContent(new Element("username").setText(username));
            fileElement.addContent(new Element("fileuser").setText(fileUser));
            fileElement.addContent(new Element("timestamp").setText(timestamp));

            filesElement.addContent(fileElement);

            guardarDocumentoXML(document);

            System.out.println("Archivo agregado exitosamente al archivo XML.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un usuario conectado al archivo XML.
     *
     * @param username El nombre de usuario del usuario conectado.
//     * @param log      El estado de conexión del usuario.
     */
    public void agregarUsuarioConectado(String username) {
        try {
            Document document = cargarDocumentoXML();

            Element loggedInUsersElement = document.getRootElement().getChild("loggedinusers");

            Element loggedInUserElement = new Element("loggedinuser");
            loggedInUserElement.addContent(new Element("username").setText(username));
//            loggedInUserElement.addContent(new Element("log").setText(log));

            loggedInUsersElement.addContent(loggedInUserElement);

            guardarDocumentoXML(document);

            System.out.println("Usuario conectado agregado exitosamente al archivo XML.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document cargarDocumentoXML() throws Exception {
        org.jdom2.input.SAXBuilder saxBuilder = new org.jdom2.input.SAXBuilder();
        return saxBuilder.build(archivoXML);
    }

    private void guardarDocumentoXML(Document document) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileWriter(archivoXML));
    }
}
