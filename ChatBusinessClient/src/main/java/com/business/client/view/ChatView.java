package com.business.client.view;

import com.business.client.model.ChatFile;
import com.business.client.model.Message;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

public class ChatView {
    public static VBox Message(Message message){

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
        messageComponent.getStyleClass().add("message-component");

        VBox.setMargin(messageComponent, new Insets(0, 0, 10, 0)); // Add bottom margin of 10 pixels
        VBox.setVgrow(messageText, Priority.ALWAYS);
        messageComponent.getChildren().addAll(topHBox, messageText);


        return messageComponent;
    }

    public static VBox MessageFile(ChatFile chatFile) throws IOException {
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

                Button pdfButton = new Button("Open PDF");
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
                    //System.out.println(Arrays.toString(storedBytes));
                });

                /* main container */
                VBox messageComponent = new VBox();
                messageComponent.getStyleClass().add("message-component");
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
                messageComponentImage.getStyleClass().add("message-component");

                VBox.setMargin(messageComponentImage, new Insets(0, 0, 10, 0)); // Add bottom margin of 10 pixels
                VBox.setVgrow(imageView, Priority.ALWAYS);
                messageComponentImage.getChildren().addAll(topHBox, imageView);
                return messageComponentImage;
        }
    }

}
