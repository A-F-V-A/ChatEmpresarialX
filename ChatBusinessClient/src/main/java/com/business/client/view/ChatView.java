package com.business.client.view;

import com.business.client.model.ChatFile;
import com.business.client.model.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.business.client.util.ValidateFile.base64ToFile;


/**
 * Clase que construye la interfaz de usuario de la vista del chat en la aplicación de cliente.
 */
public class ChatView {
    /**
     * Construye un componente de mensaje para mostrar en la vista del chat.
     *
     * @param message El mensaje a mostrar.
     * @return El componente VBox del mensaje.
     */
    public static VBox Message(Message message,boolean flag){

        /* nickname */
        Label nickname = new Label(message.getNickname());
        nickname.getStyleClass().add("message-text-nickname");
        HBox.setMargin(nickname, new Insets(0, 5, 0, 0)); // Add 5px right margin

        /* date */
        Label date = new Label(message.getDate());
        date.getStyleClass().add("message-text-date");

        /* secondary container */
        HBox topHBox = new HBox();
        topHBox.getChildren().addAll(nickname,date);

        /* messageText*/
        Label messageText = new Label(message.getContent());
        messageText.getStyleClass().add("message-text");
        messageText.setWrapText(true);

        /* main container */
        VBox messageComponent = new VBox();
        if(!flag)
           messageComponent.getStyleClass().add("message-component");
        else
            messageComponent.getStyleClass().add("message-component-propio");

        VBox.setMargin(messageComponent, new Insets(0, 0, 10, 0)); // Add bottom margin of 10 pixels
        VBox.setVgrow(messageText, Priority.ALWAYS);
        messageComponent.getChildren().addAll(topHBox, messageText);


        return messageComponent;
    }

    /**
     * Construye un componente de archivo de mensaje para mostrar en la vista del chat.
     *
     * @param chatFile El archivo de mensaje a mostrar.
     * @return El componente VBox del archivo de mensaje.
     * @throws IOException Si ocurre un error al procesar el archivo.
     */
    public static VBox MessageFile(ChatFile chatFile, boolean flag) throws IOException {
        /* nickname */
        Label nickname = new Label(chatFile.getNickname());
        nickname.getStyleClass().add("message-text-nickname");
        HBox.setMargin(nickname, new Insets(0, 5, 0, 0)); // Add 5px right margin

        /* date */
        Label date = new Label(chatFile.getDate());
        date.getStyleClass().add("message-text-date");

        /* secondary container */
        HBox topHBox = new HBox();
        topHBox.getChildren().addAll(nickname, date);


        switch (chatFile.getFileType()){
            case PDF:

                byte[] pdfBytes = base64ToFile(chatFile.getReadFileContents());
                String fileName = "src/main/resources/com/business/client/file/" + chatFile.getNickname() + ".pdf";

                Button pdfButton = new Button("Descargar PDF");
                pdfButton.getStyleClass().add("pdf-button");
                pdfButton.setUserData(pdfBytes); // Asocia los bytes del PDF al botón

                pdfButton.setOnAction(event -> {
                    Button sourceButton = (Button) event.getSource(); // Obtener el botón que desencadenó el evento
                    byte[] storedBytes = (byte[]) sourceButton.getUserData(); // Recuperar los bytes del PDF del botón
                    try {
                        Path filePath = Paths.get(fileName);
                        Files.write(filePath, storedBytes); // Escribe los bytes en un archivo en la ruta fileName
                        System.out.println("PDF saved successfully at: " + filePath);
                    } catch (IOException e) {
                        System.out.println("Error saving PDF: " + e.getMessage());
                    }
                });

                /* main container */
                VBox messageComponent = new VBox();
                if(!flag)
                    messageComponent.getStyleClass().add("message-component");
                else
                    messageComponent.getStyleClass().add("message-component-propio");
                VBox.setMargin(messageComponent, new Insets(0, 0, 10, 0));
                messageComponent.getChildren().addAll(topHBox, pdfButton);
                return messageComponent;

            default:
                byte[] imageBytes = base64ToFile(chatFile.getReadFileContents());
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(200); // Adjust the width as desired

                /* main container */
                VBox messageComponentImage = new VBox();
                if(!flag)
                    messageComponentImage.getStyleClass().add("message-component");
                else
                    messageComponentImage.getStyleClass().add("message-component-propio");

                VBox.setMargin(messageComponentImage, new Insets(0, 0, 10, 0)); // Add bottom margin of 10 pixels
                VBox.setVgrow(imageView, Priority.ALWAYS);
                messageComponentImage.getChildren().addAll(topHBox, imageView);
                return messageComponentImage;
        }
    }

    /**
     * Construye un componente de conexión de usuario para mostrar en la vista del chat.
     *
     * @param nickName El nombre de usuario conectado.
     * @return El componente HBox de conexión de usuario.
     */
    public  static  HBox UserConnect(String nickName){

        HBox hbox = new HBox(); // Crear el HBox contenedor
        hbox.setAlignment(Pos.CENTER_LEFT); // Alinear los elementos al centro izquierdo

        Label nameLabel = new Label(nickName);
        nameLabel.getStyleClass().add("username-label"); // Agregar la clase de estilo CSS al Label

        Pane circlePane = new Pane();
        circlePane.getStyleClass().add("connected-circle"); // Agregar la clase de estilo CSS al círculo
        circlePane.setPrefWidth(5);
        circlePane.setPrefHeight(5);



        // Establecer el margen alrededor del círculo
        Insets circleMargin = new Insets(0, 5, 0, 0);

        // Establecer el margen inferior en el HBox
        Insets hboxMargin = new Insets(0, 0, 5, 0);
        HBox.setMargin(hbox, hboxMargin);

        HBox.setMargin(circlePane, circleMargin);

        hbox.getChildren().addAll(circlePane, nameLabel); // Agregar el Label y el círculo al HBox

        return hbox;
    }
}
